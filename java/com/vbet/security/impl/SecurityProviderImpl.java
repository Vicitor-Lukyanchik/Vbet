package com.vbet.security.impl;

import com.vbet.dto.ProfileDto;
import com.vbet.security.SecurityProfileContainer;
import com.vbet.security.SecurityProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SecurityProviderImpl implements SecurityProvider {

    private static final String TOKEN_PATH_VALUE = "token=";
    private static final String URL_DELIMITER = "?";
    private static final String ERROR_PATH = "redirect:/error_401";
    private static final String LOGIN_PATH = "redirect:/login";
    private static final String HTTP_URL_PART = "redirect:/";

    private final SecurityProfileContainer profileContainer;

    @Override
    public String buildUrl(String path, String token) {
        if (profileContainer.isExist(token)) {
            if (path.contains(HTTP_URL_PART)) {
                return path + URL_DELIMITER + TOKEN_PATH_VALUE + token;
            } else {
                //here work with roles and direction urls
                return path;
            }
        }
        return LOGIN_PATH;
    }

    @Override
    public String authenticate(ProfileDto profile) {
        return profileContainer.add(profile);
    }

    @Override
    public void deauthenticate(String token) {
        profileContainer.delete(token);
    }

    @Override
    public void deleteCaches() {
        profileContainer.deleteCash();
    }

    @Override
    public ProfileDto getProfile(String token) {
        return profileContainer.findByToken(token);
    }
}
