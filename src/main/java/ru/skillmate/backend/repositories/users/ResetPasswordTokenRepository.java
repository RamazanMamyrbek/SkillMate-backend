package ru.skillmate.backend.repositories.users;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.skillmate.backend.entities.users.ResetPasswordToken;
import ru.skillmate.backend.entities.users.Users;

import java.util.Optional;

@Repository
public interface ResetPasswordTokenRepository extends JpaRepository<ResetPasswordToken, Long> {
    Optional<ResetPasswordToken> findByToken(String token);

    boolean existsByUser(Users user);

    Optional<ResetPasswordToken> findByUser(Users user);
}
