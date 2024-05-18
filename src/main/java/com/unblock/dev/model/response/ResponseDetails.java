package com.unblock.dev.model.response;

import com.unblock.dev.model.response.Details;
import lombok.Data;

@Data
public class ResponseDetails {
    private String message;
    private Details details;

    public ResponseDetails(String message) {
        this.message = message;
    }

    public ResponseDetails(String message, String field, String value) {
        this.message = message;
        this.details = new Details(field, value);
    }
}
