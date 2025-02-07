package ru.skillmate.backend.services.users.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.skillmate.backend.entities.users.PendingUser;
import ru.skillmate.backend.entities.users.Users;
import ru.skillmate.backend.exceptions.ResourceNotFoundException;
import ru.skillmate.backend.repositories.users.PendingUserRepository;
import ru.skillmate.backend.repositories.users.UserRepository;
import ru.skillmate.backend.services.users.UsersService;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UsersServiceImpl implements UsersService {
    private final UserRepository userRepository;
    private final PendingUserRepository pendingUserRepository;

    @Override
    @Transactional
    public PendingUser savePendingUser(PendingUser pendingUser) {
        return pendingUserRepository.save(pendingUser);
    }

    @Override
    public Users getUserByUsername(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> ResourceNotFoundException.throwUsernameNotFoundException(email));
    }

    @Override
    public PendingUser getPendingUserByEmail(String email) {
        return pendingUserRepository.findByEmail(email).orElseThrow(() -> ResourceNotFoundException.pendingUserNotFoundByEmail(email));
    }

    @Override
    @Transactional
    public Users saveUser(Users user) {
        return userRepository.save(user);
    }

    @Override
    public void deletePendingUserById(Long id) {
        PendingUser pendingUser = getPendingUserById(id);
        pendingUserRepository.delete(pendingUser);
    }

    @Override
    public PendingUser getPendingUserById(Long id) {
        return pendingUserRepository.findById(id).orElseThrow(() -> ResourceNotFoundException.pendingUserNotFoundById(id));
    }

    @Override
    public Users getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> ResourceNotFoundException.userNotFoundById(id));
    }

    @Override
    public boolean isPendingEmailUnique(String email) {
        return pendingUserRepository.findByEmail(email).isPresent();
    }

    @Override
    public boolean isEmailUnique(String email) {
        return userRepository.findByEmail(email).isPresent();
    }


}
