package com.unblock.dev.model.request;

import lombok.Data;

@Data
public class FileInfo {
    private String name;
    private String url;

    public FileInfo(String name, String url) {
        this.name = name;
        this.url = url;
    }
}
