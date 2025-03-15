package ru.skillmate.backend.dto.chats.request;

import lombok.*;
import ru.skillmate.backend.entities.chats.enums.MessageType;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatNotificationRequest {
    private String chatId;
    private String content;
    private Long senderId;
    private Long receiverId;
    private String chatName;
    private MessageType messageType;
    private NotificationType type;
    private Long resourceId;
}
