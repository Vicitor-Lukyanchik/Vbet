package com.vbet.converter.dto;

import com.vbet.dto.ProfileDto;
import com.vbet.entity.Profile;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class ProfileToDtoConverter implements Converter<Profile, ProfileDto> {

    @Override
    public ProfileDto convert(Profile p) {
        return ProfileDto.builder()
                .id(p.getId())
                .login(p.getLogin())
                .password(p.getPassword())
                .firstname(p.getFirstname())
                .lastname(p.getLastname())
                .email(p.getEmail())
                .balance(p.getBalance())
                .dob(p.getDob())
                .roles(p.getRoles())
                .transactions(p.getTransactions())
                .profileBets(p.getProfileBets())
                .message("")
                .build();
    }
}
