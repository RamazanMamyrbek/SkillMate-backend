package ru.skillmate.backend.mappers.chats;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.skillmate.backend.dto.chats.response.MessageResponseDto;
import ru.skillmate.backend.entities.chats.Message;

@Mapper(componentModel = "spring")
public interface MessageMapper {

    @Mapping(target = "id", source = "message.id")
    @Mapping(target = "content", source = "message.content")
    @Mapping(target = "messageType", source = "message.messageType")
    @Mapping(target = "messageState", source = "message.messageState")
    @Mapping(target = "senderId", source = "message.senderId")
    @Mapping(target = "receiverId", source = "message.receiverId")
    @Mapping(target = "createdAt", source = "message.createdAt") // Учитываем дату создания
    MessageResponseDto toMessageResponse(Message message);
}
