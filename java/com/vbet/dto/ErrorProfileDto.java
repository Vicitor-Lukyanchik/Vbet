package com.vbet.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class ErrorProfileDto {

    private ProfileDto profileDto;

    private Map<String, String> errors;
}
