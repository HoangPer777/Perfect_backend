package com.perfectmarket.modules.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConversationResponse {
    private UUID contactId;
    private String contactName;
    private String contactUsername;
    private String contactAvatar;
    private String lastMessage;
    private Instant lastMessageTimestamp;
    private int unreadCount;
}
