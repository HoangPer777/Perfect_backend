package com.perfectmarket.modules.chat;

import com.perfectmarket.modules.auth.User;
import com.perfectmarket.modules.auth.UserRepository;
import com.perfectmarket.modules.chat.dto.ConversationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;
    private final UserRepository userRepository;

    @Transactional
    public ChatMessage saveMessage(UUID senderId, UUID receiverId, String content) {
        User sender = userRepository.findById(senderId)
                .orElseThrow(() -> new IllegalArgumentException("Sender not found: " + senderId));
        User receiver = userRepository.findById(receiverId)
                .orElseThrow(() -> new IllegalArgumentException("Receiver not found: " + receiverId));

        ChatMessage message = ChatMessage.builder()
                .sender(sender)
                .receiver(receiver)
                .content(content)
                .build();

        return chatMessageRepository.save(message);
    }

    @Transactional
    public List<ChatMessage> getChatHistory(UUID user1, UUID user2) {
        // Đánh dấu tất cả tin nhắn đối phương gửi cho mình là đã đọc
        chatMessageRepository.markAsRead(user2, user1);
        return chatMessageRepository.findChatHistory(user1, user2);
    }

    @Transactional(readOnly = true)
    public long getUnreadCount(UUID userId) {
        return chatMessageRepository.countUnreadMessages(userId);
    }

    @Transactional(readOnly = true)
    public List<ConversationResponse> getConversations(UUID userId) {
        List<Object[]> rawList = chatMessageRepository.findConversationsRaw(userId);
        return rawList.stream().map(row -> {
            UUID contactId = UUID.fromString((String) row[0]);
            String content = (String) row[1];
            
            Instant timestamp = null;
            if (row[2] instanceof java.sql.Timestamp) {
                timestamp = ((java.sql.Timestamp) row[2]).toInstant();
            } else if (row[2] instanceof Instant) {
                timestamp = (Instant) row[2];
            } else if (row[2] != null) {
                timestamp = Instant.parse(row[2].toString());
            }

            String fullName = (String) row[3];
            String username = (String) row[4];
            String avatarUrl = (String) row[5];

            String displayName = (fullName != null && !fullName.trim().isEmpty()) ? fullName : username;
            
            int unreadCount = row[6] != null ? ((Number) row[6]).intValue() : 0;

            return ConversationResponse.builder()
                    .contactId(contactId)
                    .contactName(displayName)
                    .contactUsername(username)
                    .contactAvatar(avatarUrl)
                    .lastMessage(content)
                    .lastMessageTimestamp(timestamp)
                    .unreadCount(unreadCount)
                    .build();
        }).collect(Collectors.toList());
    }
}
