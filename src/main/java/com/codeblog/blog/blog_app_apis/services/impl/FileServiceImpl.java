package com.codeblog.blog.blog_app_apis.services.impl;

import com.codeblog.blog.blog_app_apis.services.FileService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {

    @Override
    public String uploadImage(String path, MultipartFile file) throws IOException {

        String originalName = file.getOriginalFilename();

        if (originalName == null || originalName.isBlank()) {
            throw new IllegalArgumentException("File name is invalid");
        }

        String extension = originalName.substring(originalName.lastIndexOf(".") + 1);

        List<String> allowedExtensions = List.of("png", "jpg", "jpeg");

        if (!allowedExtensions.contains(extension.toLowerCase())) {
            throw new IllegalArgumentException("Only PNG, JPG, JPEG files are allowed");
        }

        String randomId = UUID.randomUUID().toString();
        String fileName = randomId + "." + extension;

        File folder = new File(path);
        if (!folder.exists()) {
            folder.mkdirs();
        }

        String filePath = path + File.separator + fileName;

        Files.copy(file.getInputStream(),
                Paths.get(filePath),
                StandardCopyOption.REPLACE_EXISTING);

        return fileName;
    }

    @Override
    public InputStream getResource(String path, String fileName) throws FileNotFoundException {

        String fullPath = path + File.separator + fileName;
        return new FileInputStream(fullPath);
    }
}