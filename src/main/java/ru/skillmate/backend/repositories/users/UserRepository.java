package ru.skillmate.backend.repositories.users;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.skillmate.backend.entities.users.UserConstants;
import ru.skillmate.backend.entities.users.Users;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Users, Long> {
    Optional<Users> findByEmail(String email);

    boolean existsByNickname(String nickname);

    Optional<Users> findByNickname(String nickname);

    @Query(name = UserConstants.FIND_ALL_USERS_EXCEPT_SELF)
    List<Users> findAllUsersExceptSelf(@Param("publicId") Long senderId);
}
