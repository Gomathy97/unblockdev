package com.unblock.dev.model.request;

import lombok.Data;
import lombok.NonNull;

@Data
public class Auth {
    @NonNull
    private String email;

    @NonNull
    private String password;

}
