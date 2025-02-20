package ru.skillmate.backend.services.resources.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.skillmate.backend.controllers.resources.enums.ResourceType;
import ru.skillmate.backend.dto.resources.response.FileDto;
import ru.skillmate.backend.entities.resources.Resource;
import ru.skillmate.backend.exceptions.ResourceNotFoundException;
import ru.skillmate.backend.mappers.resources.ResourceMapper;
import ru.skillmate.backend.repositories.resources.ResourceRepository;
import ru.skillmate.backend.services.resources.MinioService;
import ru.skillmate.backend.services.resources.ResourceService;

import java.io.InputStream;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ResourceServiceImpl implements ResourceService {
    private final ResourceRepository resourceRepository;
    private final MinioService minioService;
    private final ResourceMapper resourceMapper;

    @Override
    public FileDto getFileDtoById(Long resourceId) {
        Resource resource = getResourceById(resourceId);
        InputStream inputStream = minioService.downloadFile(resource.getKey());
        return new FileDto(
                new InputStreamResource(inputStream),
                resource.getName(),
                getMimeType(resource.getContentType())
        );
    }

    @Override
    public Resource getResourceById(Long resourceId) {
        return resourceRepository.findById(resourceId).orElseThrow(
                () -> ResourceNotFoundException.resourceNotFoundById(resourceId)
        );
    }

    @Override
    @Transactional
    public Resource saveResource(MultipartFile file, String folder) {
        Resource resource = minioService.uploadFile(file, folder);
        return resourceRepository.save(resource);
    }

    @Override
    @Transactional
    public Resource updateResource(Long resourceId, MultipartFile file) {
        Resource oldResource = getResourceById(resourceId);
        Resource newResource = minioService.uploadFile(file, oldResource.getFolder());
        resourceMapper.updateResource(oldResource, newResource);
        resourceRepository.save(oldResource);
        return oldResource;
    }

    @Override
    @Transactional
    public void deleteResource(Long resourceId) {
        Resource resource = getResourceById(resourceId);
        minioService.deleteFile(resource.getKey());
        resourceRepository.deleteById(resourceId);
    }

    private String getMimeType(ResourceType contentType) {
            return switch (contentType) {
                case IMAGE -> "image/png";
                case VIDEO -> "video/mp4";
                case AUDIO -> "audio/mpeg";
                case PDF -> "application/pdf";
                case MSWORD -> "application/msword";
                case MSEXCEL -> "application/vnd.ms-excel";
                case ZIP -> "application/zip";
                case TEXT -> "text/plain";
                default -> "application/octet-stream";
            };
    }
}
