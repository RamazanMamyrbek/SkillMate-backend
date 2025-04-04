package ru.skillmate.backend.entities.posts;

import jakarta.persistence.*;
import lombok.*;
import ru.skillmate.backend.entities.users.Users;

@Entity
@Table(name = "post_likes", uniqueConstraints = @UniqueConstraint(name = "user_post_unique", columnNames = {"user_id", "post_id"}))
@Getter
@Setter
@ToString
@EqualsAndHashCode(of = {"id"}, callSuper = false)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostLike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private Users user;

    @ManyToOne
    @JoinColumn(name = "post_id", referencedColumnName = "id", nullable = false)
    private Post post;
}
