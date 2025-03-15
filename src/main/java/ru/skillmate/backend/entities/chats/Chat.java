package ru.skillmate.backend.entities.chats;

import jakarta.persistence.*;
import lombok.*;
import ru.skillmate.backend.entities.BaseEntity;
import ru.skillmate.backend.entities.chats.enums.MessageState;
import ru.skillmate.backend.entities.chats.enums.MessageType;
import ru.skillmate.backend.entities.users.Users;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "chats")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ToString
@NamedQuery(name = ChatConstants.FIND_CHAT_BY_SENDER_ID,
        query = "SELECT DISTINCT c FROM Chat c WHERE c.sender.id = :senderId OR c.recipient.id = :senderId ORDER BY createdAt DESC"
)
@NamedQuery(name = ChatConstants.FIND_CHAT_BY_SENDER_ID_AND_RECEIVER,
        query = "SELECT DISTINCT c FROM Chat c WHERE (c.sender.id = :senderId AND c.recipient.id = :recipientId) OR (c.sender.id = :recipientId AND c.recipient.id = :senderId) ORDER BY createdAt DESC"
)
public class Chat extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne
    @JoinColumn(name = "sender_id")
    private Users sender;

    @ManyToOne
    @JoinColumn(name = "receiver_id")
    private Users recipient;

    @OneToMany(mappedBy = "chat", fetch = FetchType.EAGER)
    @OrderBy("createdAt DESC")
    private List<Message> messages;

    @Transient
    public String getChatName(final Long senderId) {
        if(recipient.getId().equals(senderId)) {
            return sender.getFullName();
        }
        return recipient.getFullName();
    }

    @Transient
    public long getUnreadMessages(final Long senderId) {
        return messages
                .stream()
                .filter(m -> m.getReceiverId().equals(senderId))
                .filter(m -> m.getMessageState().equals(MessageState.SENT))
                .count();
    }

    @Transient
    public String getLastMessage() {
        if(messages != null && !messages.isEmpty()) {
            if(!messages.get(0).getMessageType().equals(MessageType.TEXT)) {
                return "Attachment";
            }
            return messages.get(0).getContent();
        }
        return null;
    }

    @Transient
    public LocalDateTime getLastMessageTime() {
        if(messages != null && !messages.isEmpty()) {
            messages.get(0).getCreatedAt();
        }
        return null;
    }
}

