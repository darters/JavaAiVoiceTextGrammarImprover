package com.example.aijavagrammar.dto;

import lombok.Data;

@Data
public class AiResponseInfoDTO {
    private String correctResponse;
    private String naturalnessResponse;
    private int totalTokens;
}
