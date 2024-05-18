package com.unblock.dev.model.request;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class VoteRequest {
    private int vote;
    private Long question;
    private Long answer;
}
