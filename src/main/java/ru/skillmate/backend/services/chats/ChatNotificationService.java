package ru.skillmate.backend.services.chats;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import ru.skillmate.backend.dto.chats.request.ChatNotificationRequest;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatNotificationService {

//    private final SimpMessagingTemplate messagingTemplate;

    public void sentNotification(Long userId, ChatNotificationRequest notificationRequest) {
        log.info("Sending WS notification tp {} with payload {}", userId, notificationRequest);
//        messagingTemplate.convertAndSendToUser(
//                userId.toString(),
//                "/chat",
//                notificationRequest
//        );
    }
}
