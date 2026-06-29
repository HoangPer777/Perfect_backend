package com.perfectmarket.modules.chat;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.perfectmarket.config.JwtUtil;
import com.perfectmarket.modules.auth.User;
import com.perfectmarket.modules.auth.UserRepository;
import com.perfectmarket.modules.chat.dto.ChatMessageDto;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
@RequiredArgsConstructor
public class ChatWebSocketHandler extends TextWebSocketHandler {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final ChatMessageService chatMessageService;
    private final ObjectMapper objectMapper;

    // Map user ID to set of active WebSocket sessions
    private final ConcurrentHashMap<UUID, Set<WebSocketSession>> userSessions = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String rawToken = getQueryParam(session.getUri(), "token");
        if (rawToken == null) {
            log.warn("Connection attempt without token query parameter.");
            session.close(CloseStatus.POLICY_VIOLATION.withReason("Token query parameter is missing"));
            return;
        }

        try {
            String token = URLDecoder.decode(rawToken, StandardCharsets.UTF_8);
            if (!jwtUtil.isValid(token)) {
                log.warn("Connection attempt with invalid token: {}", token);
                session.close(CloseStatus.POLICY_VIOLATION.withReason("Token is invalid or expired"));
                return;
            }

            String email = jwtUtil.extractEmail(token);
            Optional<User> userOpt = userRepository.findByEmail(email);
            if (userOpt.isEmpty()) {
                log.warn("User with email {} not found for token verification.", email);
                session.close(CloseStatus.POLICY_VIOLATION.withReason("User not found"));
                return;
            }

            User user = userOpt.get();
            UUID userId = user.getId();

            session.getAttributes().put("userId", userId);
            session.getAttributes().put("email", email);

            userSessions.computeIfAbsent(userId, k -> ConcurrentHashMap.newKeySet()).add(session);
            log.info("WebSocket connection established for user: {} ({})", user.getUsername(), userId);
        } catch (Exception e) {
            log.error("Error establishing WebSocket connection", e);
            session.close(CloseStatus.SERVER_ERROR.withReason("Internal server error during handshake"));
        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        UUID senderId = (UUID) session.getAttributes().get("userId");
        if (senderId == null) {
            session.close(CloseStatus.POLICY_VIOLATION.withReason("Not authenticated"));
            return;
        }

        try {
            ChatMessagePayload payload = objectMapper.readValue(message.getPayload(), ChatMessagePayload.class);
            if (payload.getReceiverId() == null || payload.getContent() == null || payload.getContent().trim().isEmpty()) {
                sendError(session, "Invalid message content or missing receiver ID");
                return;
            }

            // Save to database
            ChatMessage savedMessage = chatMessageService.saveMessage(senderId, payload.getReceiverId(), payload.getContent());

            // Prepare notification DTO
            ChatMessageDto messageDto = ChatMessageDto.builder()
                    .id(savedMessage.getId())
                    .senderId(savedMessage.getSender().getId())
                    .receiverId(savedMessage.getReceiver().getId())
                    .content(savedMessage.getContent())
                    .timestamp(savedMessage.getTimestamp())
                    .build();

            String responseJson = objectMapper.writeValueAsString(messageDto);
            TextMessage textResponse = new TextMessage(responseJson);

            // Send to receiver sessions
            sendMessageToUser(payload.getReceiverId(), textResponse);

            // Send to sender sessions (multi-tab sync)
            sendMessageToUser(senderId, textResponse);

        } catch (IOException e) {
            log.warn("Failed to parse or process incoming message", e);
            sendError(session, "Invalid message format. Expected JSON: {receiverId, content}");
        } catch (Exception e) {
            log.error("Error processing message", e);
            sendError(session, "Failed to send message: " + e.getMessage());
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        UUID userId = (UUID) session.getAttributes().get("userId");
        if (userId != null) {
            Set<WebSocketSession> sessions = userSessions.get(userId);
            if (sessions != null) {
                sessions.remove(session);
                if (sessions.isEmpty()) {
                    userSessions.remove(userId);
                }
            }
            log.info("WebSocket connection closed for user: {} with status: {}", userId, status);
        }
    }

    private void sendMessageToUser(UUID userId, TextMessage message) {
        Set<WebSocketSession> sessions = userSessions.get(userId);
        if (sessions != null) {
            for (WebSocketSession s : sessions) {
                if (s.isOpen()) {
                    try {
                        s.sendMessage(message);
                    } catch (IOException e) {
                        log.error("Failed to send message to user {} session {}", userId, s.getId(), e);
                    }
                }
            }
        }
    }

    private void sendError(WebSocketSession session, String errorMsg) {
        if (session.isOpen()) {
            try {
                Map<String, String> errMap = Map.of("type", "error", "message", errorMsg);
                session.sendMessage(new TextMessage(objectMapper.writeValueAsString(errMap)));
            } catch (IOException e) {
                log.error("Failed to send error response to session {}", session.getId(), e);
            }
        }
    }

    private String getQueryParam(URI uri, String paramName) {
        if (uri == null || uri.getQuery() == null) return null;
        String[] params = uri.getQuery().split("&");
        for (String param : params) {
            String[] keyValue = param.split("=");
            if (keyValue.length == 2 && keyValue[0].equals(paramName)) {
                return keyValue[1];
            }
        }
        return null;
    }

    @Data
    public static class ChatMessagePayload {
        private UUID receiverId;
        private String content;
    }
}
