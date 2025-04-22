package ru.skillmate.backend.entities.users;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ru.skillmate.backend.entities.BaseEntity;
import ru.skillmate.backend.entities.ads.Ad;
import ru.skillmate.backend.entities.ads.UserAdView;
import ru.skillmate.backend.entities.chats.Chat;
import ru.skillmate.backend.entities.resources.Resource;
import ru.skillmate.backend.entities.skills.Skill;

import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name = "users")
@Getter
@Setter
@ToString(exclude = {"imageResource", "skills", "ads", "chatsAsSender", "chatsAsRecipient"})
@EqualsAndHashCode(of = {"id", "email"}, callSuper = false)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@NamedQuery(name = UserConstants.FIND_USER_BY_EMAIL,
        query = "SELECT u FROM Users u WHERE u.email = :email"
)
@NamedQuery(name = UserConstants.FIND_ALL_USERS_EXCEPT_SELF,
        query = "SELECT u FROM Users u WHERE u.id != :publicId")
@NamedQuery(name = UserConstants.FIND_USER_BY_PUBLIC_ID,
        query = "SELECT u FROM Users u WHERE u.id = :publicId")
public class Users extends BaseEntity implements UserDetails {
    private static final int LAST_ACTIVE_INTERVAL = 5;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "email", unique = true, nullable = false)
    private String email;
    @Column(name = "nickname", unique = true, nullable = false)
    private String nickname;
    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @Column(name = "country", nullable = false)
    private String country;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(name = "gender", nullable = false)
    private Gender gender = Gender.UNDEFINED;

    @Column(name = "city")
    private String city;

    @OneToOne
    @JoinColumn(name = "image_resource_id", referencedColumnName = "id")
    private Resource imageResource;

    @OneToMany(mappedBy = "user")
    private List<Skill> skills = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<Ad> ads = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<UserAdView> viewedAds = new ArrayList<>();

    private LocalDateTime lastSeen;

    @OneToMany(mappedBy = "sender")
    private List<Chat> chatsAsSender;

    @OneToMany(mappedBy = "recipient")
    private List<Chat> chatsAsRecipient;

    @ManyToMany
    @JoinTable(
            name = "user_subscriptions",
            joinColumns = @JoinColumn(name = "follower_id"),
            inverseJoinColumns = @JoinColumn(name = "followed_id"),
            uniqueConstraints = @UniqueConstraint(name = "uk_user_subscription_unique",
            columnNames = {"follower_id", "followed_id"})
    )
    private Set<Users> following = new HashSet<>();

    @ManyToMany(mappedBy = "following")
    private Set<Users> followers = new HashSet<>();


    @Transient
    public boolean isUserOnline() {
        return lastSeen != null && lastSeen.isAfter(LocalDateTime.now().minusMinutes(LAST_ACTIVE_INTERVAL));
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return passwordHash;
    }

    @Override
    public String getUsername() {
        return email;
    }
}
