package com.unblock.dev.model.request;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
public class ViewRequest {
    @NonNull
    private int view;
    private Long question;
    private Long answer;
}
