package ru.skillmate.backend.mappers.chats;

import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import ru.skillmate.backend.dto.chats.response.ChatResponse;
import ru.skillmate.backend.entities.chats.Chat;

@Mapper(componentModel = "spring")
public interface ChatMapper {

    @Mapping(target = "name", source = "chat", qualifiedByName = "mapToChatName")
    @Mapping(target = "id", source = "chat.id")
    @Mapping(target = "unreadCount", expression = "java(chat.getUnreadMessages(userId))")
    @Mapping(target = "lastMessage", expression = "java(chat.getLastMessage())")
    @Mapping(target = "lastMessageTime", expression = "java(chat.getLastMessageTime())")
    @Mapping(target = "senderId", source = "chat.sender.id")
    @Mapping(target = "receiverId", source = "chat.recipient.id")
    ChatResponse mapToChatResponseDto(Chat chat, @Context Long userId);

    @Named("mapToChatName")
    default String mapToChatName(Chat chat, @Context Long userId) {
        return chat.getChatName(userId);
    }
}