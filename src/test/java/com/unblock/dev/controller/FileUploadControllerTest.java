package com.unblock.dev.controller;

import com.unblock.dev.model.request.FileInfo;
import com.unblock.dev.model.response.Response;
import com.unblock.dev.service.FileUploadService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class FileUploadControllerTest {
    @Mock
    private FileUploadService fileUploadService;

    @InjectMocks
    private FileUploadController fileUploadController;

    @Before
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testUploadProfileImage() {
        MultipartFile file1 = mock(MultipartFile.class);
        MultipartFile file2 = mock(MultipartFile.class);
        when(file1.getOriginalFilename()).thenReturn("file1.jpg");
        when(file2.getOriginalFilename()).thenReturn("file2.png");
        when(fileUploadService.save(any(MultipartFile.class))).thenReturn("/path/to/file");

        ResponseEntity response = fileUploadController.uploadProfileImage(new MultipartFile[]{file1, file2});

        assertEquals(HttpStatus.OK, response.getStatusCode());
        List<FileInfo> fileInfoList = (List<FileInfo>) response.getBody();
        assertEquals(2, fileInfoList.size());
        assertEquals("file1.jpg", fileInfoList.get(0).getName());
        assertEquals("/path/to/file", fileInfoList.get(0).getUrl());
        assertEquals("file2.png", fileInfoList.get(1).getName());
        assertEquals("/path/to/file", fileInfoList.get(1).getUrl());

        verify(fileUploadService, times(2)).save(any(MultipartFile.class));
    }

    @Test
    public void testUploadProfileImageError() {
        MultipartFile file = mock(MultipartFile.class);
        when(fileUploadService.save(any(MultipartFile.class))).thenThrow(new RuntimeException());

        ResponseEntity response = fileUploadController.uploadProfileImage(new MultipartFile[]{file});

        assertEquals(HttpStatus.EXPECTATION_FAILED, response.getStatusCode());
        Response responseBody = (Response) response.getBody();
        assertEquals("Fail to upload files!", responseBody.getMessage());

        verify(fileUploadService, times(1)).save(any(MultipartFile.class));
    }

}
