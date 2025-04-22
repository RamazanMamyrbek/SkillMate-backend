package ru.skillmate.backend.repositories.skills;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.skillmate.backend.entities.skills.Skill;
import ru.skillmate.backend.entities.users.Users;

import java.util.List;
import java.util.Optional;

@Repository
public interface SkillRepository extends JpaRepository<Skill, Long> {
    List<Skill> findAllByUserId(Long userId);

    boolean existsByNameAndUser(String skillName, Users user);

    Optional<Skill> findByNameAndUser(String name, Users user);
}
