package ru.skillmate.backend.services.users.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.skillmate.backend.dto.users.request.ProfileEditRequestDto;
import ru.skillmate.backend.dto.users.response.UserProfileResponseDto;
import ru.skillmate.backend.entities.resources.Resource;
import ru.skillmate.backend.entities.users.PendingUser;
import ru.skillmate.backend.entities.users.Users;
import ru.skillmate.backend.exceptions.ResourceAlreadyTakenException;
import ru.skillmate.backend.exceptions.ResourceNotFoundException;
import ru.skillmate.backend.mappers.users.UsersMapper;
import ru.skillmate.backend.repositories.users.PendingUserRepository;
import ru.skillmate.backend.repositories.users.UserRepository;
import ru.skillmate.backend.services.resources.ResourceService;
import ru.skillmate.backend.services.users.UsersService;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UsersServiceImpl implements UsersService {
    private final UserRepository userRepository;
    private final PendingUserRepository pendingUserRepository;
    private final UsersMapper usersMapper;
    private final ResourceService resourceService;
    @Value("${minio.folders.profileImages}")
    private String folderProfileImages;

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

    @Override
    public UserProfileResponseDto getProfileInfo(String email) {
        Users user = getUserByUsername(email);
        return usersMapper.userToUserResponseDto(user);
    }

    @Override
    @Transactional
    public UserProfileResponseDto editProfile(String email, ProfileEditRequestDto profileEditRequestDto) {
        Users user = getUserByUsername(email);
        checkUserNicknameForEdit(user.getNickname(), profileEditRequestDto.nickname());
        usersMapper.updateUserProfile(user, profileEditRequestDto);
        userRepository.save(user);
        return usersMapper.userToUserResponseDto(user);
    }

    @Override
    @Transactional
    public UserProfileResponseDto editProfileImage(String email, MultipartFile image) {
        Users user = getUserByUsername(email);
        Resource oldImageResource = user.getImageResource();
        if(oldImageResource != null) {
            resourceService.deleteResource(oldImageResource.getId());
        }
        Resource newImageResource = resourceService.saveResource(image, folderProfileImages);
        user.setImageResource(newImageResource);
        return usersMapper.userToUserResponseDto(userRepository.save(user));
    }

    private void checkUserNicknameForEdit(String nickname, String newNickname) {
        if(userRepository.existsByNickname(newNickname) && !userRepository.findByNickname(nickname).get().getNickname().equals(newNickname)) {
            throw ResourceAlreadyTakenException.userNicknameWasAlreadyTaken(newNickname);
        }
    }


}
