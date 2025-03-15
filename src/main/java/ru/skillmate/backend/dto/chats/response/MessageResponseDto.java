package ru.skillmate.backend.dto.chats.response;

import lombok.*;
import ru.skillmate.backend.entities.chats.enums.MessageState;
import ru.skillmate.backend.entities.chats.enums.MessageType;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MessageResponseDto {
    private Long id;
    private String content;
    private MessageType messageType;
    private MessageState messageState;
    private Long senderId;
    private Long receiverId;
    private LocalDateTime createdAt;
    private Long resourceId;

}
