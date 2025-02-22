package ru.skillmate.backend.mappers.skills;

import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import ru.skillmate.backend.dto.skills.response.SkillResponseDto;
import ru.skillmate.backend.entities.resources.Resource;
import ru.skillmate.backend.entities.skills.Skill;
import ru.skillmate.backend.entities.skills.enums.SkillLevel;
import ru.skillmate.backend.entities.users.Users;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SkillMapper {
    @IterableMapping(qualifiedByName = "mapToResponseDto")
    List<SkillResponseDto> mapToResponseDtoList(List<Skill> skillList);

    @Named("mapToResponseDto")
    @Mapping(target = "level", source = "level", qualifiedByName = "convertEnumToString")
    @Mapping(target = "achievementIds", source = "achievements", qualifiedByName = "convertResourceToId")
    @Mapping(target = "userId", source = "user", qualifiedByName = "convertUserToId")
    SkillResponseDto mapToResponseDto(Skill skill);

    @Named("convertEnumToString")
    default String convertEnumToString(SkillLevel level) {
        return level.name();
    }

    @Named("convertResourceToId")
    default Long convertResourceToId(Resource resource) {
        return resource != null ? resource.getId() : null;
    }


    @Named("convertUserToId")
    default Long convertUserToId(Users user) {
        return user != null ? user.getId() : null;
    }
}
