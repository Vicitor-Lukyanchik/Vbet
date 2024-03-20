package com.vbet.security;

import com.vbet.dto.ProfileDto;
import com.vbet.entity.Profile;

public interface SecurityProfileContainer {

    String add(ProfileDto profile);

    void delete(String token);

    ProfileDto findByToken(String token);
    boolean isExist(String token);

    void deleteCash();
}
