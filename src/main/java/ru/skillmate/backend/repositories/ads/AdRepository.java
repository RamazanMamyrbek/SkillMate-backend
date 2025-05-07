package ru.skillmate.backend.repositories.ads;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.skillmate.backend.entities.ads.Ad;
import ru.skillmate.backend.entities.users.Users;

import java.util.List;
import java.util.Optional;

@Repository
public interface AdRepository extends JpaRepository<Ad, Long>, JpaSpecificationExecutor<Ad> {

    List<Ad> findTop20BySkillNameInAndUserIdNot(List<String> recommendedSkills, Long userId);

    boolean existsBySkillNameAndUser(String skillName, Users user);

    Optional<Ad> findBySkillNameAndUser(String skillName, Users user);

    List<Ad> findAllByUserId(long userId);
}
