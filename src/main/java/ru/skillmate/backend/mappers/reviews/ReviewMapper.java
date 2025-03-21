package ru.skillmate.backend.mappers.reviews;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import ru.skillmate.backend.dto.reviews.request.ReviewCreateRequestDto;
import ru.skillmate.backend.dto.reviews.response.ReviewResponseDto;
import ru.skillmate.backend.entities.reviews.Review;
import ru.skillmate.backend.entities.users.Users;

@Mapper(componentModel = "spring")
public interface ReviewMapper {
    @Mapping(target = "reviewerId", source = "reviewer", qualifiedByName = "mapUserToId")
    @Mapping(target = "recipientId", source = "recipient", qualifiedByName = "mapUserToId")
    ReviewResponseDto toReviewResponseDto(Review reviewResponseDto);

    @Mapping(target = "reviewer", source = "reviewer")
    @Mapping(target = "recipient", source = "recipient")
    @Mapping(target = "id", ignore = true)
    Review mapToReviewEntity(ReviewCreateRequestDto reviewCreateRequestDto, Users reviewer, Users recipient);

    @Named("mapUserToId")
    default Long mapUserToId(Users user) {
        return user != null ? user.getId() : null;
    }
}
