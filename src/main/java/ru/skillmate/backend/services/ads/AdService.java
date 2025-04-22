package ru.skillmate.backend.services.ads;

import org.springframework.data.domain.PageRequest;
import org.springframework.web.multipart.MultipartFile;
import ru.skillmate.backend.dto.ads.response.AdResponseDto;
import ru.skillmate.backend.dto.common.PageResponseDto;
import ru.skillmate.backend.entities.ads.Ad;

import java.util.List;

public interface AdService {

    AdResponseDto createAd(Long userId, String skillName, String description, MultipartFile imageResource, String email);

    AdResponseDto getAdResponseDtoById(Long adId, String email);

    Ad getAdById(Long adId);

    AdResponseDto editAd(Long adId, Long userId, String skillName, String description, MultipartFile imageResource, String name);

    void deleteAd(Long adId, String email);

    PageResponseDto<AdResponseDto> searchAds(PageRequest pageRequest, String searchValue, List<String> country, List<String> city, List<String> level);

    List<AdResponseDto> getRecommendationsForUser(String email);
}
