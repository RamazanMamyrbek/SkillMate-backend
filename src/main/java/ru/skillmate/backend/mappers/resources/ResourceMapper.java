package ru.skillmate.backend.mappers.resources;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ru.skillmate.backend.entities.resources.Resource;

@Mapper(componentModel = "spring")
public interface ResourceMapper {
    @Mapping(target = "id", ignore = true)
    void updateResource(@MappingTarget Resource oldResource,Resource newResource);
}
