package com.perfectmarket.modules.chat;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, UUID> {

    @Query("SELECT m FROM ChatMessage m WHERE " +
           "(m.sender.id = :user1 AND m.receiver.id = :user2) OR " +
           "(m.sender.id = :user2 AND m.receiver.id = :user1) " +
           "ORDER BY m.timestamp ASC")
    List<ChatMessage> findChatHistory(@Param("user1") UUID user1, @Param("user2") UUID user2);

    @Query(value = "SELECT CAST(contact_id AS VARCHAR) as contactId, content, timestamp, " +
           "u.full_name as fullName, u.username as username, u.avatar_url as avatarUrl, " +
           "CAST((SELECT COUNT(*) FROM chat_messages WHERE sender_id = contact_id AND receiver_id = :userId AND is_read = false) AS INTEGER) as unreadCount " +
           "FROM (" +
           "  SELECT DISTINCT ON (contact_id) " +
           "    CASE WHEN sender_id = :userId THEN receiver_id ELSE sender_id END as contact_id, " +
           "    content, " +
           "    timestamp " +
           "  FROM chat_messages " +
           "  WHERE sender_id = :userId OR receiver_id = :userId " +
           "  ORDER BY contact_id, timestamp DESC" +
           ") sub " +
           "JOIN users u ON u.id = sub.contact_id " +
           "ORDER BY timestamp DESC", nativeQuery = true)
    List<Object[]> findConversationsRaw(@Param("userId") UUID userId);

    @Query("SELECT COUNT(m) FROM ChatMessage m WHERE m.receiver.id = :userId AND m.isRead = false")
    long countUnreadMessages(@Param("userId") UUID userId);

    @Modifying
    @Transactional
    @Query("UPDATE ChatMessage m SET m.isRead = true WHERE m.sender.id = :senderId AND m.receiver.id = :receiverId AND m.isRead = false")
    void markAsRead(@Param("senderId") UUID senderId, @Param("receiverId") UUID receiverId);
}
