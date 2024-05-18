package com.unblock.dev.controller;

import com.unblock.dev.model.request.FileInfo;
import com.unblock.dev.model.response.Response;
import com.unblock.dev.service.FileUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("api")
public class FileUploadController {

    @Autowired
    public FileUploadService storageService;

    @PostMapping("/upload_files")
    ResponseEntity uploadProfileImage(@RequestParam("files") MultipartFile[] files) {
        try {
            List<FileInfo> fileInfo = new ArrayList<>();

            Arrays.stream(files).forEach(file -> {
                String path = storageService.save(file);
                fileInfo.add(new FileInfo(file.getOriginalFilename(), path));
            });

            return ResponseEntity.status(HttpStatus.OK).body(fileInfo);
        } catch (RuntimeException e) {
            String message = "Fail to upload files!";
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new Response(message));
        }
    }
}
