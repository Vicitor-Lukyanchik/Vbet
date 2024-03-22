package com.vbet.security.impl;

import com.vbet.dto.ProfileDto;
import com.vbet.dto.SecurityProfile;
import com.vbet.entity.Profile;
import com.vbet.exception.SecurityException;
import com.vbet.security.SecurityProfileContainer;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class SecurityProfileContainerImpl implements SecurityProfileContainer {

    private static final int ACTIVE_PROFILE_TOKENS = 4;
    private static final int MAX_TOKEN_LIVE_HOURS_WITH_LAST = 3;
    private static final int MAX_TOKEN_LIVE_HOURS = 24;
    private static Random random = new Random();

    private List<SecurityProfile> profiles = new ArrayList<>();

    @Override
    public String add(ProfileDto profile) {
        checkOverloadedProfiles(profile);
        SecurityProfile securityProfile = createSecurityProfile(profile);
        profiles.add(0, securityProfile);
        return securityProfile.getToken();
    }

    private void checkOverloadedProfiles(ProfileDto profile) {
        List<SecurityProfile> checkedProfiles = new ArrayList<>();
        for (SecurityProfile value : profiles) {
            if (profile.equals(value.getProfile())) {
                checkedProfiles.add(value);
            }
        }
        checkedProfiles = checkedProfiles.stream().sorted(Comparator.comparing(SecurityProfile::getLocalDateTime)).collect(Collectors.toList());
        for (SecurityProfile securityProfile : checkedProfiles) {
            profiles.remove(securityProfile);
        }

        if (checkedProfiles.size() > ACTIVE_PROFILE_TOKENS) {
            checkedProfiles = checkedProfiles.subList(0, ACTIVE_PROFILE_TOKENS);
        }

        profiles.addAll(checkedProfiles.subList(0, getStartNotValidIndex(checkedProfiles)));
    }

    private int getStartNotValidIndex(List<SecurityProfile> checkedProfiles) {
        int startNotValidIndex = checkedProfiles.size();
        for (int i = 1; i < checkedProfiles.size(); i++) {
            if (checkedProfiles.get(i).getLocalDateTime().isBefore(LocalDateTime.now().minusHours(MAX_TOKEN_LIVE_HOURS_WITH_LAST))) {
                startNotValidIndex = i;
            }
        }
        return startNotValidIndex;
    }

    private SecurityProfile createSecurityProfile(ProfileDto profile) {
        return SecurityProfile.builder()
                .localDateTime(LocalDateTime.now())
                .profile(profile)
                .token(buildToken(profile))
                .build();
    }

    private String buildToken(ProfileDto profile) {
        List<SecurityProfile> allByProfile = findAllByProfile(profile);
        String token = generateToken(profile.getLogin());

        while (findEqualToken(allByProfile, token).isPresent()) {
            token = generateToken(profile.getLogin());
        }

        return token;
    }

    private List<SecurityProfile> findAllByProfile(ProfileDto profile) {
        return profiles.stream().filter(p -> p.getProfile().equals(profile)).collect(Collectors.toList());
    }

    private static Optional<SecurityProfile> findEqualToken(List<SecurityProfile> allByProfile, String token) {
        return allByProfile.stream().filter(p -> p.getToken().equals(token)).findAny();
    }

    private String generateToken(String login) {
        int hashCode = login.hashCode();
        int lastTokenPart = random.nextInt(89999) + 10000;
        return hashCode + "R" + lastTokenPart;
    }

    @Override
    public void delete(String token) {
        Optional<SecurityProfile> securityProfile = profiles.stream().filter(p -> p.getToken().equals(token)).findAny();
        securityProfile.ifPresent(profile -> profiles.remove(profile));
    }

    @Override
    public ProfileDto findByToken(String token) {
        Optional<SecurityProfile> securityProfile = profiles.stream().filter(p -> p.getToken().equals(token)).findAny();
        if(securityProfile.isEmpty()){
            throw new SecurityException("There is not authentificated profile with token =" + token);
        }
        return securityProfile.get().getProfile();
    }

    @Override
    public boolean isExist(String token) {
        Optional<SecurityProfile> securityProfile = profiles.stream().filter(p -> p.getToken().equals(token)).findAny();
        return securityProfile.isPresent();
    }

    @Override
    public void deleteCash() {
        List<SecurityProfile> deletedProfiles = new ArrayList<>();
        for (SecurityProfile profile : profiles) {
            if (profile.getLocalDateTime().isBefore(LocalDateTime.now().minusHours(MAX_TOKEN_LIVE_HOURS))) {
                deletedProfiles.add(profile);
            }
        }
        for (SecurityProfile deletedProfile : deletedProfiles) {
            profiles.remove(deletedProfile);
        }
    }
}
