package com.unblock.dev.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.core.io.UrlResource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class FileUploadServiceTest {
    @InjectMocks
    private FileUploadService fileUploadService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testSaveAndLoad() throws IOException {
        MultipartFile multipartFile = new MockMultipartFile("test1.txt", "test1.txt",
                "file", "Hello World".getBytes());

        String savedFilePath = fileUploadService.save(multipartFile);

        assertTrue(Files.exists(Paths.get(savedFilePath)));

        UrlResource loadedResource = fileUploadService.load(multipartFile.getOriginalFilename());

        assertNotNull(loadedResource);

        Files.deleteIfExists(Paths.get(savedFilePath));

        assertFalse(Files.exists(Paths.get(savedFilePath)));
    }

    @Test
    public void testLoadAll() throws IOException {
        MultipartFile multipartFile = new MockMultipartFile("test1.txt", "test1.txt",
                "file", "Hello World 1".getBytes());

        fileUploadService.save(multipartFile);

        Stream<Path> loadedFiles = fileUploadService.loadAll();

        assertTrue(loadedFiles.anyMatch(path -> path.toString().endsWith("test1.txt")));

        Files.deleteIfExists(Paths.get(FileUploadService.UPLOAD_DIRECTORY + "test1.txt"));
    }
}
