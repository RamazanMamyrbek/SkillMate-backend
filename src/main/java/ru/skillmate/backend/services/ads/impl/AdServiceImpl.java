package ru.skillmate.backend.services.ads.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.skillmate.backend.dto.ads.response.AdResponseDto;
import ru.skillmate.backend.dto.common.PageResponseDto;
import ru.skillmate.backend.entities.ads.Ad;
import ru.skillmate.backend.entities.resources.Resource;
import ru.skillmate.backend.entities.skills.Skill;
import ru.skillmate.backend.entities.users.Users;
import ru.skillmate.backend.exceptions.ResourceNotFoundException;
import ru.skillmate.backend.exceptions.ResourcesNotMatchingException;
import ru.skillmate.backend.mappers.ads.AdMapper;
import ru.skillmate.backend.repositories.ads.AdRepository;
import ru.skillmate.backend.services.ads.AdService;
import ru.skillmate.backend.services.resources.ResourceService;
import ru.skillmate.backend.services.users.UsersService;
import ru.skillmate.backend.spec.AdSpecifications;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdServiceImpl implements AdService {
    private final AdRepository adRepository;
    private final ResourceService resourceService;
    private final UsersService usersService;
    private final AdMapper adMapper;
    @Value("${minio.folders.adImages}")
    private String folderAdsImages;

    @Override
    public PageResponseDto<AdResponseDto> searchAds(PageRequest pageRequest, String searchValue, List<String> countries, List<String> cities, List<String> levels) {
        Specification<Ad> spec = AdSpecifications.bySearchFilters(searchValue, countries, cities, levels);
        Page<Ad> adPage = adRepository.findAll(spec, pageRequest);
        List<AdResponseDto> content = adPage.getContent().stream()
                .map(adMapper::mapToResponseDto)
                .toList();

        return new PageResponseDto<>(
                content,
                adPage.getNumber()+1,
                adPage.getTotalPages(),
                adPage.getTotalElements(),
                adPage.getSize()
        );
    }

    @Override
    @Transactional
    public AdResponseDto createAd(Long userId, String skillName, String description, MultipartFile imageResource, String email) {
        Users user = usersService.getUserById(userId);
        checkSkillNameAndUser(skillName, user, email);
        resourceService.checkImage(imageResource);
        Resource savedImageResource = resourceService.saveResource(imageResource, folderAdsImages);
        Ad ad = Ad
                .builder()
                .skillName(skillName)
                .description(description)
                .imageResource(savedImageResource)
                .country(user.getCountry())
                .city(user.getCity())
                .level(user.getSkills().stream().filter(skill -> skill.getName().equals(skillName)).findFirst().get().getLevel())
                .user(user)
                .build();
        return adMapper.mapToResponseDto(adRepository.save(ad));
    }

    @Override
    public AdResponseDto getAdResponseDtoById(Long adId) {
        return adMapper.mapToResponseDto(getAdById(adId));
    }

    @Override
    public Ad getAdById(Long adId) {
        return adRepository.findById(adId).orElseThrow(() -> ResourceNotFoundException.adNotFound(adId));
    }

    @Override
    @Transactional
    public AdResponseDto editAd(Long adId, Long userId, String skillName, String description, MultipartFile imageResource, String email) {
        Ad ad = getAdById(adId);
        Users user = usersService.getUserById(userId);
        checkAdBelongsToUser(ad, user);
        checkSkillNameAndUser(skillName, user, email);
        ad.setSkillName(skillName);
        ad.setDescription(description);
        if(imageResource != null && !imageResource.isEmpty()) {
            resourceService.checkImage(imageResource);
            Resource savedImageResource = ad.getImageResource();
            savedImageResource = resourceService.updateResource(savedImageResource.getId(), imageResource);
            ad.setImageResource(savedImageResource);
        }
        return adMapper.mapToResponseDto(ad);
    }

    @Override
    @Transactional
    public void deleteAd(Long adId, String email) {
        Ad ad = getAdById(adId);
        checkAdBelongsToUser(ad, (Users) usersService.getUserByUsername(email));
        resourceService.deleteResource(ad.getImageResource().getId());
        adRepository.deleteById(adId);
    }

    private void checkAdBelongsToUser(Ad ad, Users user) {
        if(!user.getAds().contains(ad)) {
            throw ResourcesNotMatchingException.adDoesNottBelongToUser(ad.getId(), user.getId());
        }
    }

    private void checkSkillNameAndUser(String skillName, Users user, String email) {
        if(!email.equals(user.getEmail())) {
            throw ResourcesNotMatchingException.userEmailAndIdNotMatching(email, user.getId());
        }
        if(!user.getSkills().stream().map(Skill::getName).toList()
                .contains(skillName)) {
            throw ResourcesNotMatchingException.skillNameNotValidForAd(skillName, user.getId());
        }
    }
}
