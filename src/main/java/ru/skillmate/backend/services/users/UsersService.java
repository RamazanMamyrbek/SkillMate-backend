package ru.skillmate.backend.services.users;

import org.springframework.security.core.userdetails.UserDetails;
import ru.skillmate.backend.entities.users.PendingUser;
import ru.skillmate.backend.entities.users.Users;

public interface UsersService {
    PendingUser savePendingUser(PendingUser pendingUser);

    UserDetails getUserByUsername(String username);

    PendingUser getPendingUserByEmail(String email);

    Users saveUser(Users user);

    void deletePendingUserById(Long id);

    PendingUser getPendingUserById(Long id);
    Users getUserById(Long id);

    boolean isPendingEmailUnique(String email);

    boolean isEmailUnique(String email);
}
