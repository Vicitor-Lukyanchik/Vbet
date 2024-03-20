package com.vbet.service;

import com.vbet.dto.AuthenticationRequestDto;
import com.vbet.dto.ErrorProfileDto;
import com.vbet.dto.ProfileDto;
import com.vbet.dto.RegisterRequestDto;
import com.vbet.entity.Profile;

import java.util.List;

public interface ProfileService {
    ErrorProfileDto login(AuthenticationRequestDto requestDto);

    List<ProfileDto> findAll();

    ErrorProfileDto register(RegisterRequestDto registerDto);

    ProfileDto findByLogin(String login);

    ProfileDto findById(Long id);

    void deleteById(Long id);
}
