package ru.skillmate.backend.mappers.posts;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.skillmate.backend.dto.posts.response.PostResponseDto;
import ru.skillmate.backend.entities.posts.Post;

@Mapper(componentModel = "spring")
public interface PostMapper {

    @Mapping(target = "creatorId", source = "creator.id")
    @Mapping(target = "creatorNickname", source = "creator.nickname")
    @Mapping(target = "creatorFullName", source = "creator.fullName")
    @Mapping(target = "resourceId", source = "resource.id")
    PostResponseDto toPostResponseDto(Post post);

}
