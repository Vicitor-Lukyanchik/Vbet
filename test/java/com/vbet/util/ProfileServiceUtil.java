package com.vbet.util;

import com.vbet.dto.ProfileDto;
import com.vbet.dto.RegisterRequestDto;
import com.vbet.entity.Profile;
import com.vbet.entity.Role;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ProfileServiceUtil {

    public static Profile getProfile(){
        return Profile.builder()
                .id(1l)
                .login("user")
                .password("$2a$12$5FFM8eTRdp1AFEyvimVWG.iNv5kIYvARy2bzb/bB4S9E4YTWENdsa")
                .firstname("Victor")
                .lastname("Lukyanchik")
                .email("victor@mail.com")
                .balance(new BigDecimal(50))
                .dob(LocalDate.now())
                .roles(getRoles())
                .transactions(new ArrayList<>())
                .profileBets(new ArrayList<>())
                .build();
    }
    public static ProfileDto getProfileDto(){
        return ProfileDto.builder()
                .id(1l)
                .login("user")
                .password("user")
                .firstname("Victor")
                .lastname("Lukyanchik")
                .email("victor@mail.com")
                .balance(new BigDecimal(50))
                .dob(LocalDate.now())
                .roles(getRoles())
                .transactions(new ArrayList<>())
                .profileBets(new ArrayList<>())
                .build();
    }

public static ProfileDto getProfileResultDto(){
        return ProfileDto.builder()
                .id(1l)
                .login("user")
                .password("$2a$12$5FFM8eTRdp1AFEyvimVWG.iNv5kIYvARy2bzb/bB4S9E4YTWENdsa")
                .firstname("Victor")
                .lastname("Lukyanchik")
                .email("victor@mail.com")
                .balance(new BigDecimal(50))
                .dob(LocalDate.now())
                .roles(getRoles())
                .transactions(new ArrayList<>())
                .profileBets(new ArrayList<>())
                .message("")
                .build();
    }

 public static RegisterRequestDto getRegisterRequestDto(){
        return RegisterRequestDto.builder()
                .login("user")
                .password("user")
                .repeatPassword("user")
                .email("victor@mail.com")
                .build();
    }

    public static List<Role> getRoles(){
        return Arrays.asList(Role.builder().id(1l).name("ROLE_USER").build());
    }
}
