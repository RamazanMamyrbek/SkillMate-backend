package ru.skillmate.backend.services.resources;

import org.springframework.web.multipart.MultipartFile;
import ru.skillmate.backend.entities.resources.Resource;

import java.io.InputStream;

public interface MinioService {
    InputStream downloadFile(String key);

    void deleteFile(String key);

    Resource uploadFile(MultipartFile file, String folder);
}
