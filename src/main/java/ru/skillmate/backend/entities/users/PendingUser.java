package ru.skillmate.backend.entities.users;

import jakarta.persistence.*;
import lombok.*;
import ru.skillmate.backend.entities.BaseEntity;

@Entity
@Table(name = "pending_users")
@Getter
@Setter
@ToString
@EqualsAndHashCode(of = {"id", "email"}, callSuper = false)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PendingUser extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @Column(name = "country", nullable = false)
    private String country;

    @Column(name = "email_confirmation_code")
    private String emailConfirmationCode;
}
