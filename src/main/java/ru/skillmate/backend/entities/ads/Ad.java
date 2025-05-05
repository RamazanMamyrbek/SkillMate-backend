package ru.skillmate.backend.entities.ads;

import jakarta.persistence.*;
import lombok.*;
import ru.skillmate.backend.entities.BaseEntity;
import ru.skillmate.backend.entities.resources.Resource;
import ru.skillmate.backend.entities.skills.enums.SkillLevel;
import ru.skillmate.backend.entities.users.Users;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ads", uniqueConstraints = @UniqueConstraint(columnNames = {"skill_name", "user_id"}))
@Getter
@Setter
@ToString(exclude = {"user"})
@EqualsAndHashCode(of = {"id"}, callSuper = false)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Ad extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "skill_name")
    private String skillName;

    @Column(length = 2000)
    private String description;

    private String country;

    private String city;

    @Enumerated(EnumType.STRING)
    private SkillLevel level;

    @OneToOne
    @JoinColumn(name = "image_resource_id", referencedColumnName = "id")
    private Resource imageResource;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private Users user;

    @Builder.Default
    @OneToMany(mappedBy = "ad", cascade = CascadeType.ALL, orphanRemoval = true)
    List<UserAdView> views = new ArrayList<>();
}
