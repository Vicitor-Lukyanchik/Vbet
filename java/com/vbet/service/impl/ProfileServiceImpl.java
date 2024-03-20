package com.vbet.service.impl;

import com.vbet.converter.dto.ProfileToDtoConverter;
import com.vbet.dto.AuthenticationRequestDto;
import com.vbet.dto.ErrorProfileDto;
import com.vbet.dto.ProfileDto;
import com.vbet.dto.RegisterRequestDto;
import com.vbet.entity.Profile;
import com.vbet.repository.ProfileRepository;
import com.vbet.service.ProfileService;
import com.vbet.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.util.*;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
@Validated
public class ProfileServiceImpl implements ProfileService {

    private static final String INVALID_PASSWORD_MESSAGE = "Неверный пароль";
    private static final String INVALID_PASSWORDS_MESSAGE = "Пароли должны совпадать";
    private static final String INVALID_EMAIL_MESSAGE = "Email некорректно введен";
    private static final String EMAIL_REGEX_PATTERN = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
            + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";;
    private static final String PROFILE_NOT_FOUND_BY_USERNAME = "Не найден пользователь с логином : ";
    private static final String PROFILE_FOUND_BY_USERNAME = "Пользователь уже найден с логином : ";
    private static final String PROFILE_NOT_FOUND_BY_ID = "Не найден пользователь с id : ";
    private static final String MIN_SIZE_MESSAGE = " должен составлять не менее 4 символов";
    private static final String LOGIN = "Логин";
    private static final String PASSWORD = "Пароль";
    private static final String EMAIL = "Email";
    private static final String CANT_BE_EMPTY = " не может быть пустым";
    private static final String EMPTY_STRING = "";
    private static final Integer MIN_LOGIN_SIZE = 4;
    private static final Integer MIN_PASSWORD_SIZE = 4;
    private static final BigDecimal START_BALANCE = new BigDecimal(4);
    private static final String START_ROLE_NAME = "ROLE_USER";

    private final ProfileRepository profileRepository;
    private final RoleService roleService;
    private final ProfileToDtoConverter converter;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    @Transactional
    public ErrorProfileDto login(AuthenticationRequestDto requestDto) {
        Map<String, String> errors = generateValidatedMessage(requestDto);
        if (!errors.isEmpty()) {
            return ErrorProfileDto.builder().errors(errors).build();
        }
        ProfileDto profileDto = findByLogin(requestDto.getLogin());
        if (profileDto.getMessage().isEmpty() && !bCryptPasswordEncoder.matches(requestDto.getPassword(), profileDto.getPassword())) {
            errors.put("password", INVALID_PASSWORD_MESSAGE);
            return ErrorProfileDto.builder().errors(errors).build();
        }
        return ErrorProfileDto.builder().errors(errors).profileDto(profileDto).build();
    }

    @Override
    public List<ProfileDto> findAll() {
        List<Profile> profiles = profileRepository.findAll();
        List<ProfileDto> result = new ArrayList<>();
        for (Profile profile : profiles) {
            result.add(converter.convert(profile));
        }
        return result;
    }

    @Override
    public ErrorProfileDto register(RegisterRequestDto registerDto) {
        Map<String, String> errors =  validateProfile(registerDto);
        if (!errors.isEmpty()) {
            return ErrorProfileDto.builder().errors(errors).build();
        }
        return ErrorProfileDto.builder()
                .errors(errors)
                .profileDto(converter.convert(profileRepository.save(buildProfile(registerDto))))
                .build();
    }

    private Map<String, String> validateProfile(RegisterRequestDto registerDto) {
        Map<String, String> errors = generateValidatedMessage(AuthenticationRequestDto.builder().login(registerDto.getLogin())
                .password(registerDto.getPassword()).build());
        ProfileDto userByUsername = findByLogin(registerDto.getLogin());

        if (userByUsername.getMessage().isEmpty()) {
            errors.put("login", PROFILE_FOUND_BY_USERNAME + registerDto.getLogin());
        }

        if (!registerDto.getPassword().equals(registerDto.getRepeatPassword())) {
            errors.put("repeatPassword", INVALID_PASSWORDS_MESSAGE + registerDto.getLogin());
        }

        if (registerDto.getEmail().isEmpty()) {
            errors.put("email", EMAIL + CANT_BE_EMPTY);
        } else if (patternMatches(registerDto.getEmail())) {
            errors.put("email",INVALID_EMAIL_MESSAGE );
        }
        return errors;
    }

    private boolean patternMatches(String emailAddress) {
        return Pattern.compile(EMAIL_REGEX_PATTERN)
                .matcher(emailAddress)
                .matches();
    }

    private Map<String, String> generateValidatedMessage(AuthenticationRequestDto dto) {
        String password = dto.getPassword();
        String login = dto.getLogin();
        Map<String, String> errors = new HashMap<>();

        if (password.isEmpty()) {
            errors.put("password", PASSWORD + CANT_BE_EMPTY);
        } else if (password.length() < MIN_PASSWORD_SIZE) {
            errors.put("password", PASSWORD + MIN_SIZE_MESSAGE);
        }
        if (login.isEmpty()) {
            errors.put("login", LOGIN + CANT_BE_EMPTY);
        } else if (login.length() < MIN_LOGIN_SIZE) {
            errors.put("login", LOGIN + MIN_SIZE_MESSAGE);
        }

        return errors;
    }

    private Profile buildProfile(RegisterRequestDto registerDto) {
        Profile profile = new Profile();
        Profile.builder()
                .login(registerDto.getLogin())
                .password(bCryptPasswordEncoder.encode(registerDto.getPassword()))
                .firstname(EMPTY_STRING)
                .lastname(EMPTY_STRING)
                .email(registerDto.getEmail())
                .balance(START_BALANCE)
                .dob(null)
                .roles(roleService.findByName(START_ROLE_NAME))
                .transactions(new ArrayList<>())
                .profileBets(new ArrayList<>())
                .build();
        return profile;
    }

    @Override
    public ProfileDto findByLogin(String login) {
        Optional<Profile> profile = profileRepository.findByLogin(login);

        if (profile.isEmpty() || !login.equals(profile.get().getLogin())) {
            return ProfileDto.builder().message(PROFILE_NOT_FOUND_BY_USERNAME + login).build();
        }
        return converter.convert(profile.get());
    }

    @Override
    public ProfileDto findById(Long id) {
        Optional<Profile> profile = profileRepository.findById(id);
        if (profile.isEmpty()) {
            return ProfileDto.builder().message(PROFILE_NOT_FOUND_BY_ID + id).build();
        }
        return converter.convert(profile.get());
    }

    @Override
    public void deleteById(Long id) {
        profileRepository.deleteById(id);
    }
}
