package ru.skillmate.backend.services.chats;

import lombok.RequiredArgsConstructor;
import org.checkerframework.checker.units.qual.C;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.skillmate.backend.dto.chats.response.ChatResponse;
import ru.skillmate.backend.entities.chats.Chat;
import ru.skillmate.backend.entities.users.Users;
import ru.skillmate.backend.mappers.chats.ChatMapper;
import ru.skillmate.backend.repositories.chats.ChatRepository;
import ru.skillmate.backend.services.users.UsersService;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatService {
    private final ChatRepository chatRepository;
    private final UsersService usersService;
    private final ChatMapper chatMapper;

    @Transactional(readOnly = true)
    public List<ChatResponse> getChatsByReceiverId(Principal principal) {
        final Long userId = usersService.getUserByEmail(principal.getName()).getId();
        return chatRepository.findChatsBySenderId(userId)
                .stream()
                .map(chat -> chatMapper.mapToChatResponseDto(chat, userId))
                .toList();
    }

    @Transactional
    public String createChat(Long senderId, Long receiverId) {
        Optional<Chat> existingChat = chatRepository.findChatBySenderAndReceiver(senderId, receiverId);
        if(existingChat.isPresent()) {
            return existingChat.get().getId();
        }
        Users sender = usersService.getUserById(senderId);
        Users receiver = usersService.getUserById(receiverId);
        Chat chat = new Chat();
        chat.setSender(sender);
        chat.setRecipient(receiver);
        chat = chatRepository.save(chat);
        return chat.getId();
    }
}
