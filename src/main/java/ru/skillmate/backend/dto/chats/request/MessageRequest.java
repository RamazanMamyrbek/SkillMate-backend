package ru.skillmate.backend.dto.chats.request;

import lombok.*;
import ru.skillmate.backend.entities.chats.enums.MessageType;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MessageRequest {
    private String content;
    private Long senderId;
    private Long receiverId;
    private MessageType messageType;
    private String chatId;
}
