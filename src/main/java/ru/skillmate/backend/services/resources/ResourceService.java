package ru.skillmate.backend.services.resources;

import org.springframework.web.multipart.MultipartFile;
import ru.skillmate.backend.dto.resources.response.FileDto;
import ru.skillmate.backend.entities.resources.Resource;

public interface ResourceService {
    FileDto getFileDtoById(Long resourceId);

    Resource getResourceById(Long resourceId);

    Resource saveResource(MultipartFile file, String folder);

    Resource updateResource(Long resourceId, MultipartFile file);

    void deleteResource(Long resourceId);

    void checkImage(MultipartFile file);

    void checkFile(MultipartFile file);

}
