package com.perfectmarket.modules.chat;

import com.perfectmarket.modules.auth.UserPrincipal;
import com.perfectmarket.modules.chat.dto.ConversationResponse;
import com.perfectmarket.modules.chat.dto.ChatMessageDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatMessageService chatMessageService;

    @GetMapping("/conversations")
    public ResponseEntity<List<ConversationResponse>> getConversations(@AuthenticationPrincipal UserPrincipal principal) {
        List<ConversationResponse> conversations = chatMessageService.getConversations(principal.id());
        return ResponseEntity.ok(conversations);
    }

    @GetMapping("/unread-count")
    public ResponseEntity<java.util.Map<String, Long>> getUnreadCount(@AuthenticationPrincipal UserPrincipal principal) {
        long count = chatMessageService.getUnreadCount(principal.id());
        return ResponseEntity.ok(java.util.Map.of("unreadCount", count));
    }

    @GetMapping("/history/{contactId}")
    public ResponseEntity<List<ChatMessageDto>> getChatHistory(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable UUID contactId) {
        List<ChatMessage> messages = chatMessageService.getChatHistory(principal.id(), contactId);
        List<ChatMessageDto> dtos = messages.stream().map(m -> ChatMessageDto.builder()
                .id(m.getId())
                .senderId(m.getSender().getId())
                .receiverId(m.getReceiver().getId())
                .content(m.getContent())
                .timestamp(m.getTimestamp())
                .build()).collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }
}
