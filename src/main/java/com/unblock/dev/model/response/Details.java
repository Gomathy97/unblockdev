package com.unblock.dev.model.response;

import lombok.Data;

@Data
public class Details {
    private String field;
    private String value;

    public Details(String field, String value) {
        this.field = field;
        this.value = value;
    }
}
