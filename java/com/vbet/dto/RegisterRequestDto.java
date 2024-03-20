package com.vbet.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RegisterRequestDto {

    private String login;

    private String password;

    private String email;

    private String repeatPassword;
}
