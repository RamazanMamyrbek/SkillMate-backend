package ru.skillmate.backend.entities.ads;

import jakarta.persistence.*;
import lombok.*;
import ru.skillmate.backend.entities.BaseEntity;
import ru.skillmate.backend.entities.ads.enums.ExchangeStatus;
import ru.skillmate.backend.entities.users.Users;

@Entity
@Table(name = "exchange_requests", uniqueConstraints = @UniqueConstraint(columnNames = {"requester_id", "ad_id", "receiver_id"}))
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExchangeRequest extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "requester_id", referencedColumnName = "id", nullable = false)
    private Users requester;

    @ManyToOne
    @JoinColumn(name = "receiver_id", referencedColumnName = "id", nullable = false)
    private Users receiver;

    @ManyToOne
    @JoinColumn(name = "ad_id", referencedColumnName = "id", nullable = false)
    private Ad ad;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    @Builder.Default
    private ExchangeStatus exchangeStatus = ExchangeStatus.PENDING;

    @Column(name = "message", length = 2000)
    private String message;

}
