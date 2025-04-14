package ru.skillmate.backend.services.users;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.multipart.MultipartFile;
import ru.skillmate.backend.dto.users.request.ProfileEditRequestDto;
import ru.skillmate.backend.dto.users.response.UserProfileResponseDto;
import ru.skillmate.backend.entities.users.PendingUser;
import ru.skillmate.backend.entities.users.Users;

import java.security.Principal;
import java.util.List;

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

    UserProfileResponseDto getProfileInfo(String email);

    UserProfileResponseDto editProfile(String email, ProfileEditRequestDto profileEditRequestDto);

    UserProfileResponseDto editProfileImage(String email, MultipartFile image);

    UserProfileResponseDto getUserInfo(Long userId);

    Users getUserByEmail(String email);

    List<UserProfileResponseDto> getAllUsersExceptSelf(Principal principal);

    List<UserProfileResponseDto> getAllUsers();

}
