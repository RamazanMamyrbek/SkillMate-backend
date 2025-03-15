package ru.skillmate.backend.repositories.chats;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.skillmate.backend.dto.chats.response.ChatResponse;
import ru.skillmate.backend.entities.chats.Chat;
import ru.skillmate.backend.entities.chats.ChatConstants;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRepository extends JpaRepository<Chat, String > {
    @Query(name = ChatConstants.FIND_CHAT_BY_SENDER_ID)
    List<Chat> findChatsBySenderId(@Param("senderId") Long userId);

    @Query(name = ChatConstants.FIND_CHAT_BY_SENDER_ID_AND_RECEIVER)
    Optional<Chat> findChatBySenderAndReceiver(@Param("senderId") Long senderId, @Param("recipientId") Long receiverId);
}
