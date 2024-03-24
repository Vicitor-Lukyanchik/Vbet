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
import org.apache.commons.validator.routines.EmailValidator;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
@Validated
public class ProfileServiceImpl implements ProfileService {

    private static final String INVALID_PASSWORD_MESSAGE = "Неверный пароль";
    private static final String AT_LEAST_ONE_ALPHABETIC_MESSAGE = "Хотя бы одна буква";
    private static final String INVALID_SYMBOLS_MESSAGE = "Присутствуют некорректные символы";
    private static final String INVALID_PASSWORDS_MESSAGE = "Пароли должны совпадать";
    private static final String INVALID_EMAIL_MESSAGE = "Email некорректно введен";
    private static final String PROFILE_NOT_FOUND_BY_LOGIN_MESSAGE = "Пользователь с таким логином не найден";
    private static final String PROFILE_FOUND_BY_USERNAME_MESSAGE = "Пользователь с таким логином уже найден";
    private static final String PROFILE_NOT_FOUND_BY_ID_MESSAGE = "Пользователь с таким id не найден : ";
    private static final String MIN_SIZE_MESSAGE = "Не менее";
    private static final String MAX_SIZE_MESSAGE = "Не более";
    private static final String SYMBOLS_MESSAGE = "символов";
    private static final String CANT_BE_EMPTY_MESSAGE = "Не может быть пустым";
    private static final String SPACE = " ";
    private static final String EMPTY_STRING = "";
    private static final Integer MIN_LOGIN_SIZE = 4;
    private static final Integer MAX_LOGIN_SIZE = 20;
    private static final Integer MIN_PASSWORD_SIZE = 4;
    private static final Integer MAX_PASSWORD_SIZE = 60;
    private static final Integer MAX_EMAIL_SIZE = 150;
    private static final BigDecimal START_BALANCE = new BigDecimal(50);
    private static final String START_ROLE_NAME = "ROLE_USER";
    private static final String MAIN_REGEX = "^[0-9A-Za-z\\s-_.]+$";
    private static final String AT_LEAST_ONE_ALPHABETIC_REGEX = ".*[a-zA-Z]+.*";


    private final ProfileRepository profileRepository;
    private final RoleService roleService;
    private final ProfileToDtoConverter converter;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    @Transactional
    public ErrorProfileDto login(AuthenticationRequestDto requestDto) {
        Map<String, String> errors = generateLoginPasswordValidMessages(requestDto.getLogin(), requestDto.getPassword());
        if (!errors.isEmpty()) {
            return ErrorProfileDto.builder().errors(errors).build();
        }
        return generateLoginValidMessages(requestDto.getLogin(), requestDto.getPassword());
    }

    private ErrorProfileDto generateLoginValidMessages(String login, String password) {
        Map<String, String> errors = new HashMap<>();
        ProfileDto profileDto = findByLogin(login);
        if (!profileDto.getMessage().isEmpty()) {
            errors.put("login", PROFILE_NOT_FOUND_BY_LOGIN_MESSAGE);
        } else if (!bCryptPasswordEncoder.matches(password, profileDto.getPassword())) {
            errors.put("password", INVALID_PASSWORD_MESSAGE);
        }
        return ErrorProfileDto.builder().errors(errors).profileDto(profileDto).build();
    }

    @Override
    public ErrorProfileDto register(RegisterRequestDto registerDto) {
        Map<String, String> errors = generateRegisterValidMessages(registerDto);
        if (!errors.isEmpty()) {
            return ErrorProfileDto.builder().errors(errors).build();
        }
        return ErrorProfileDto.builder()
                .errors(errors)
                .profileDto(converter.convert(profileRepository.save(buildProfile(registerDto))))
                .build();
    }

    private Map<String, String> generateRegisterValidMessages(RegisterRequestDto registerDto) {
        Map<String, String> errors = generateLoginPasswordValidMessages(registerDto.getLogin(), registerDto.getPassword());
        errors.putAll(generateEmailValidMessage(registerDto.getEmail()));

        ProfileDto userByUsername = findByLogin(registerDto.getLogin());
        if (userByUsername.getMessage().isEmpty()) {
            errors.put("login", PROFILE_FOUND_BY_USERNAME_MESSAGE);
        }
        if (!registerDto.getPassword().equals(registerDto.getRepeatPassword())) {
            errors.put("repeatPassword", INVALID_PASSWORDS_MESSAGE);
        }

        return errors;
    }

    private Map<String, String> generateEmailValidMessage(String email) {
        Map<String, String> errors = new HashMap<>();
        if (email.isEmpty()) {
            errors.put("email", CANT_BE_EMPTY_MESSAGE);
        } else if (email.length() > MAX_EMAIL_SIZE) {
            errors.put("email", MAX_SIZE_MESSAGE + SPACE + MAX_EMAIL_SIZE + SPACE + SYMBOLS_MESSAGE);
        } else if (!EmailValidator.getInstance().isValid(email)) {
            errors.put("email", INVALID_EMAIL_MESSAGE);
        }
        return errors;
    }

    private Map<String, String> generateLoginPasswordValidMessages(String login, String password) {
        Map<String, String> errors = new HashMap<>();
        errors.putAll(generateTextValidMessage(login, "login", MIN_LOGIN_SIZE, MAX_LOGIN_SIZE, MAIN_REGEX));
        errors.putAll(generateTextValidMessage(password, "password", MIN_PASSWORD_SIZE, MAX_PASSWORD_SIZE, MAIN_REGEX));
        return errors;
    }

    private Map<String, String> generateTextValidMessage(String text, String errorMessage, int minSize, int maxSize, String regex) {
        Map<String, String> errors = new HashMap<>();
        if (text.isEmpty()) {
            errors.put(errorMessage, CANT_BE_EMPTY_MESSAGE);
        } else if (text.length() < minSize) {
            errors.put(errorMessage, MIN_SIZE_MESSAGE + SPACE + minSize + SPACE + SYMBOLS_MESSAGE);
        } else if (text.length() > maxSize) {
            errors.put(errorMessage, MAX_SIZE_MESSAGE + SPACE + maxSize + SPACE + SYMBOLS_MESSAGE);
        } else if (!text.matches(regex)) {
            errors.put(errorMessage, INVALID_SYMBOLS_MESSAGE);
        } else if (!text.matches(AT_LEAST_ONE_ALPHABETIC_REGEX)) {
            errors.put(errorMessage, AT_LEAST_ONE_ALPHABETIC_MESSAGE);
        }
        return errors;
    }

    private Profile buildProfile(RegisterRequestDto registerDto) {
        return Profile.builder()
                .login(registerDto.getLogin())
                .password(bCryptPasswordEncoder.encode(registerDto.getPassword()))
                .firstname(EMPTY_STRING)
                .lastname(EMPTY_STRING)
                .email(registerDto.getEmail())
                .balance(START_BALANCE)
                .dob(LocalDate.now())
                .roles(roleService.findByName(START_ROLE_NAME))
                .transactions(new ArrayList<>())
                .profileBets(new ArrayList<>())
                .build();
    }

    @Override
    public ProfileDto findByLogin(String login) {
        Optional<Profile> profile = profileRepository.findByLogin(login);

        if (profile.isEmpty() || !login.equals(profile.get().getLogin())) {
            return ProfileDto.builder().message(PROFILE_NOT_FOUND_BY_LOGIN_MESSAGE).build();
        }
        return converter.convert(profile.get());
    }

    @Override
    public ProfileDto findById(Long id) {
        Optional<Profile> profile = profileRepository.findById(id);
        if (profile.isEmpty()) {
            return ProfileDto.builder().message(PROFILE_NOT_FOUND_BY_ID_MESSAGE + id).build();
        }
        return converter.convert(profile.get());
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
    public void deleteById(Long id) {
        profileRepository.deleteById(id);
    }
}
