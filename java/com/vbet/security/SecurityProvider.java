package com.vbet.security;

import com.vbet.dto.ProfileDto;
import com.vbet.entity.Profile;

public interface SecurityProvider {

    String buildUrl(String path, String token);

    String authenticate(ProfileDto profile);

    void deauthenticate(String token);

    void deleteCaches();

    ProfileDto getProfile(String token);
}
