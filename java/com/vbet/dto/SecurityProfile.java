package com.vbet.dto;

import com.vbet.entity.Profile;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class SecurityProfile {

    private String token;

    private ProfileDto profile;

    private LocalDateTime localDateTime;
}
