package ru.skillmate.backend.entities.ads;

import jakarta.persistence.*;
import lombok.*;
import ru.skillmate.backend.entities.BaseEntity;
import ru.skillmate.backend.entities.users.Users;

@Entity
@Table(name = "user_ad_views", uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "ad_id"}))
@Getter
@Setter
@ToString
@EqualsAndHashCode(of = {"id"}, callSuper = false)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserAdView extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private Users user;

    @ManyToOne
    @JoinColumn(name = "ad_id", referencedColumnName = "id")
    private Ad ad;

}
