package ru.skillmate.backend.entities.users;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ru.skillmate.backend.entities.BaseEntity;
import ru.skillmate.backend.entities.resources.Resource;
import ru.skillmate.backend.entities.skills.Skill;

import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
@ToString
@EqualsAndHashCode(of = {"id", "email"}, callSuper = false)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Users extends BaseEntity implements UserDetails {
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
    private List<Skill> skills;

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

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }
}
