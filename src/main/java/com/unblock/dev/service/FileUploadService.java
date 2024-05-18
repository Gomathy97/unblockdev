package com.unblock.dev.service;

import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

@Service
public class FileUploadService {
    public static String UPLOAD_DIRECTORY = System.getProperty("user.dir") + "/src/main/resources/static/";
    private final Path root = Paths.get(UPLOAD_DIRECTORY);
//    public void init() {
//        try {
//            Files.createDirectory(root);
//        } catch (IOException e) {
//            throw new RuntimeException("Could not initialize folder for upload!");
//        }
//    }

    public String save(MultipartFile file) {
        try {
            String path = root.resolve(file.getOriginalFilename()).toString();
            Files.copy(file.getInputStream(), root.resolve(file.getOriginalFilename()));
            return path;
        } catch (IOException e) {
            throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
        }
    }

    public UrlResource load(String filename) {
        try {
            Path file = root.resolve(filename);
            UrlResource resource = new UrlResource(file.toUri());

            return resource;
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }

    public Stream<Path> loadAll() {
        try {
            return Files.walk(root, 1).filter(path -> !path.equals(root)).map(root::relativize);
        } catch (IOException e) {
            throw new RuntimeException("Could not load the files!");
        }
    }
}
