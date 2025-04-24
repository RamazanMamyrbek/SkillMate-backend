package ru.skillmate.backend.services.chats;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.skillmate.backend.dto.chats.request.ChatNotificationRequest;
import ru.skillmate.backend.dto.chats.request.MessageRequest;
import ru.skillmate.backend.dto.chats.request.NotificationType;
import ru.skillmate.backend.dto.chats.response.MessageResponseDto;
import ru.skillmate.backend.entities.chats.Chat;
import ru.skillmate.backend.entities.chats.Message;
import ru.skillmate.backend.entities.chats.enums.MessageState;
import ru.skillmate.backend.entities.chats.enums.MessageType;
import ru.skillmate.backend.entities.resources.Resource;
import ru.skillmate.backend.entities.users.Users;
import ru.skillmate.backend.exceptions.FileException;
import ru.skillmate.backend.exceptions.ResourceNotFoundException;
import ru.skillmate.backend.mappers.chats.MessageMapper;
import ru.skillmate.backend.repositories.chats.ChatRepository;
import ru.skillmate.backend.repositories.chats.MessageRepository;
import ru.skillmate.backend.services.resources.ResourceService;
import ru.skillmate.backend.services.users.UsersService;

import java.security.Principal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MessageService {
    private final MessageRepository messageRepository;
    private final ChatRepository chatRepository;
    private final MessageMapper messageMapper;
    private final UsersService usersService;
    private final ResourceService resourceService;
    @Value("${minio.folders.chats}")
    private String chatFolder;
    private final ChatNotificationService notificationService;


    @Transactional
    public void saveMessage(MessageRequest messageRequest) {
        Chat chat = chatRepository.findById(messageRequest.getChatId())
                .orElseThrow(() -> ResourceNotFoundException.chatNotFound(messageRequest.getChatId()));
        Message message = new Message();
        message.setContent(messageRequest.getContent());
        message.setChat(chat);
        message.setSenderId(messageRequest.getSenderId());
        message.setReceiverId(messageRequest.getReceiverId());
        message.setMessageType(messageRequest.getMessageType());
        message.setMessageState(MessageState.SENT);
        message = messageRepository.save(message);

        ChatNotificationRequest notificationRequest = ChatNotificationRequest
                .builder()
                .chatId(chat.getId())
                .content(messageRequest.getContent())
                .messageType(messageRequest.getMessageType())
                .senderId(message.getSenderId())
                .receiverId(message.getReceiverId())
                .type(NotificationType.MESSAGE)
                .chatName(chat.getChatName(message.getSenderId()))
                .build();
        notificationService.sentNotification(messageRequest.getReceiverId(), notificationRequest);
    }

    @Transactional
    public void uploadFileMessage(String chatId, MultipartFile file, Principal principal) {
        Chat chat = chatRepository.findById(chatId).orElseThrow(() -> ResourceNotFoundException.chatNotFound(chatId));
        Long senderId = getSenderId(chat, principal);
        Long recipientId = getRecipientId(chat, principal);
        Resource resource = resourceService.saveResource(file, chatFolder);
        Message message = new Message();
        message.setChat(chat);
        message.setSenderId(senderId);
        message.setReceiverId(recipientId);
        message.setMessageType(getContentType(file));
        message.setMessageState(MessageState.SENT);
        message.setResourceId(resource.getId());
        message = messageRepository.save(message);

        ChatNotificationRequest notificationRequest = ChatNotificationRequest
                .builder()
                .chatId(chat.getId())
                .messageType(MessageType.FILE)
                .senderId(senderId)
                .receiverId(recipientId)
                .type(NotificationType.MESSAGE)
                .resourceId(resource.getId())
                .build();
        notificationService.sentNotification(recipientId, notificationRequest);
    }


    public List<MessageResponseDto> findChatMessages(String chatId) {
        return messageRepository.findMessagesByChatId(chatId)
                .stream()
                .map(messageMapper::toMessageResponse)
                .toList();
    }

    @Transactional
    public void setMessagesToSeen(String chatId, Principal principal) {
        Chat chat = chatRepository.findById(chatId).orElseThrow(() -> ResourceNotFoundException.chatNotFound(chatId));
        Long recipientId = getRecipientId(chat, principal);
        messageRepository.setMessagesToSeenByChatId(chat.getId(), MessageState.SEEN);

        ChatNotificationRequest notificationRequest = ChatNotificationRequest
                .builder()
                .chatId(chat.getId())
                .senderId(getSenderId(chat, principal))
                .receiverId(recipientId)
                .type(NotificationType.SEEN)
                .build();
        notificationService.sentNotification(recipientId, notificationRequest);
    }

    private MessageType getContentType(MultipartFile file) {
        if(file == null || file.isEmpty()) {
            throw new FileException("File is empty");
        }else if(file.getContentType().equals("image/png") || file.getContentType().equals("image/jpeg")) {
            return MessageType.IMAGE;
        } else {
            return MessageType.FILE;
        }
    }

    private Long getSenderId(Chat chat, Principal principal) {
        Users user = usersService.getUserByEmail(principal.getName());
        if(chat.getSender().getId().equals(user.getId())) {
            return chat.getSender().getId();
        }
        return chat.getRecipient().getId();
    }

    private Long getRecipientId(Chat chat, Principal principal) {
        Users user = usersService.getUserByEmail(principal.getName());
        if(chat.getSender().getId().equals(user.getId())) {
            return chat.getRecipient().getId();
        }
        return chat.getSender().getId();
    }
}
