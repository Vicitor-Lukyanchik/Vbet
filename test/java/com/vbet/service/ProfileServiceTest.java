package com.vbet.service;

import com.vbet.dto.AuthenticationRequestDto;
import com.vbet.dto.ErrorProfileDto;
import com.vbet.dto.RegisterRequestDto;
import com.vbet.entity.Profile;
import com.vbet.entity.Role;
import com.vbet.repository.ProfileRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.vbet.util.ProfileServiceUtil.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.BDDMockito.given;
@SpringBootTest
public class ProfileServiceTest {

    @Autowired
    private ProfileService profileService;

    @MockBean
    private RoleService roleService;

    @MockBean
    private ProfileRepository profileRepository;

    @Test
    public void login_ShouldReturnErrorWithLogin_WhenLoginIsEmpty() {
        Map<String, String> errors = new HashMap<>();
        errors.put("login","Не может быть пустым");
        ErrorProfileDto expected = ErrorProfileDto.builder().errors(errors).build();


        AuthenticationRequestDto authenticationRequestDto = AuthenticationRequestDto.builder()
                .login("").password("user").build();
        ErrorProfileDto actual = profileService.login(authenticationRequestDto);

        assertEquals(expected, actual);
    }

    @Test
    public void login_ShouldReturnErrorWithLogin_WhenLoginSizeLessThan4() {
        Map<String, String> errors = new HashMap<>();
        errors.put("login","Не менее 4 символов");
        ErrorProfileDto expected = ErrorProfileDto.builder().errors(errors).build();


        AuthenticationRequestDto authenticationRequestDto = AuthenticationRequestDto.builder()
                .login("use").password("user").build();
        ErrorProfileDto actual = profileService.login(authenticationRequestDto);

        assertEquals(expected, actual);
    }

    @Test
    public void login_ShouldReturnErrorWithLogin_WhenLoginSizeMoreThan20() {
        Map<String, String> errors = new HashMap<>();
        errors.put("login","Не более 20 символов");
        ErrorProfileDto expected = ErrorProfileDto.builder().errors(errors).build();


        AuthenticationRequestDto authenticationRequestDto = AuthenticationRequestDto.builder()
                .login("usuriweouroiweuroweiruweouroiwuee").password("user").build();
        ErrorProfileDto actual = profileService.login(authenticationRequestDto);

        assertEquals(expected, actual);
    }

    @Test
    public void login_ShouldReturnErrorWithLogin_WhenLoginHaveInvalidSymbols() {
        Map<String, String> errors = new HashMap<>();
        errors.put("login","Присутствуют некорректные символы");
        ErrorProfileDto expected = ErrorProfileDto.builder().errors(errors).build();


        AuthenticationRequestDto authenticationRequestDto = AuthenticationRequestDto.builder()
                .login("use^").password("user").build();
        ErrorProfileDto actual = profileService.login(authenticationRequestDto);

        assertEquals(expected, actual);
    }

    @Test
    public void login_ShouldReturnErrorWithLogin_WhenLoginHasNotLetter() {
        Map<String, String> errors = new HashMap<>();
        errors.put("login","Хотя бы одна буква");
        ErrorProfileDto expected = ErrorProfileDto.builder().errors(errors).build();


        AuthenticationRequestDto authenticationRequestDto = AuthenticationRequestDto.builder()
                .login("_._69_._").password("user").build();
        ErrorProfileDto actual = profileService.login(authenticationRequestDto);

        assertEquals(expected, actual);
    }

    @Test
    public void login_ShouldReturnErrorWithPassword_WhenPasswordIsEmpty() {
        Map<String, String> errors = new HashMap<>();
        errors.put("password","Не может быть пустым");
        ErrorProfileDto expected = ErrorProfileDto.builder().errors(errors).build();


        AuthenticationRequestDto authenticationRequestDto = AuthenticationRequestDto.builder()
                .login("user").password("").build();
        ErrorProfileDto actual = profileService.login(authenticationRequestDto);

        assertEquals(expected, actual);
    }

    @Test
    public void login_ShouldReturnErrorWithPassword_WhenPasswordSizeLessThan4() {
        Map<String, String> errors = new HashMap<>();
        errors.put("password","Не менее 4 символов");
        ErrorProfileDto expected = ErrorProfileDto.builder().errors(errors).build();


        AuthenticationRequestDto authenticationRequestDto = AuthenticationRequestDto.builder()
                .login("user").password("use").build();
        ErrorProfileDto actual = profileService.login(authenticationRequestDto);

        assertEquals(expected, actual);
    }

    @Test
    public void login_ShouldReturnErrorWithPassword_WhenPasswordSizeMoreThan60() {
        Map<String, String> errors = new HashMap<>();
        errors.put("password","Не более 60 символов");
        ErrorProfileDto expected = ErrorProfileDto.builder().errors(errors).build();


        AuthenticationRequestDto authenticationRequestDto = AuthenticationRequestDto.builder()
                .login("user").password("usuriweouroiweurowfsdfdfdfeifjdskljfalkjflsjdflkjruweouroiwuee").build();
        ErrorProfileDto actual = profileService.login(authenticationRequestDto);

        assertEquals(expected, actual);
    }

    @Test
    public void login_ShouldReturnErrorWithPassword_WhenPasswordHaveInvalidSymbols() {
        Map<String, String> errors = new HashMap<>();
        errors.put("password","Присутствуют некорректные символы");
        ErrorProfileDto expected = ErrorProfileDto.builder().errors(errors).build();


        AuthenticationRequestDto authenticationRequestDto = AuthenticationRequestDto.builder()
                .login("use.r_52-1").password("user$").build();
        ErrorProfileDto actual = profileService.login(authenticationRequestDto);

        assertEquals(expected, actual);
    }

    @Test
    public void login_ShouldReturnErrorWithPassword_WhenPasswordHasNotLetter() {
        Map<String, String> errors = new HashMap<>();
        errors.put("password","Хотя бы одна буква");
        ErrorProfileDto expected = ErrorProfileDto.builder().errors(errors).build();


        AuthenticationRequestDto authenticationRequestDto = AuthenticationRequestDto.builder()
                .login("user").password("_._69_._").build();
        ErrorProfileDto actual = profileService.login(authenticationRequestDto);

        assertEquals(expected, actual);
    }

    @Test
    public void login_ShouldReturnErrorWithLoginAndPassword_WhenLoginAndPasswordIsEmpty() {
        Map<String, String> errors = new HashMap<>();
        errors.put("login","Не может быть пустым");
        errors.put("password","Не может быть пустым");
        ErrorProfileDto expected = ErrorProfileDto.builder().errors(errors).build();


        AuthenticationRequestDto authenticationRequestDto = AuthenticationRequestDto.builder()
                .login("").password("").build();
        ErrorProfileDto actual = profileService.login(authenticationRequestDto);

        assertEquals(expected.getErrors(), actual.getErrors());
    }

    @Test
    public void login_ShouldReturnErrorWithLogin_WhenLoginNotFounded() {
        Map<String, String> errors = new HashMap<>();
        errors.put("login","Пользователь с таким логином не найден");
        ErrorProfileDto expected = ErrorProfileDto.builder().errors(errors).build();

        List<Role> roles = getRoles();
        given(roleService.findByName(isA(String.class))).willReturn(roles);
        given(profileRepository.findByLogin(isA(String.class))).willReturn(Optional.empty());

        AuthenticationRequestDto authenticationRequestDto = AuthenticationRequestDto.builder()
                .login("use.r_52-1").password("user").build();
        ErrorProfileDto actual = profileService.login(authenticationRequestDto);

        assertEquals(expected.getErrors(), actual.getErrors());
    }

    @Test
    public void login_ShouldReturnErrorWithPassword_WhenPasswordsNotEquals() {
        Map<String, String> errors = new HashMap<>();
        errors.put("password","Неверный пароль");
        ErrorProfileDto expected = ErrorProfileDto.builder().errors(errors).build();

        List<Role> roles = getRoles();
        given(roleService.findByName(isA(String.class))).willReturn(roles);
        Profile profile = Profile.builder().login("user").password("user1").build();
        given(profileRepository.findByLogin(isA(String.class))).willReturn(Optional.of(profile));

        AuthenticationRequestDto authenticationRequestDto = AuthenticationRequestDto.builder()
                .login("user").password("user").build();
        ErrorProfileDto actual = profileService.login(authenticationRequestDto);

        assertEquals(expected.getErrors(), actual.getErrors());
    }

    @Test
    public void login_ShouldReturnErrorProfileDto_WhenAllIsCorrect() {
        Map<String, String> errors = new HashMap<>();
        ErrorProfileDto expected = ErrorProfileDto.builder().profileDto(getProfileResultDto()).errors(errors).build();

        List<Role> roles = getRoles();
        given(roleService.findByName(isA(String.class))).willReturn(roles);
        given(profileRepository.findByLogin(isA(String.class))).willReturn(Optional.of(getProfile()));

        AuthenticationRequestDto authenticationRequestDto = AuthenticationRequestDto.builder()
                .login("user").password("user").build();
        ErrorProfileDto actual = profileService.login(authenticationRequestDto);

        assertEquals(expected, actual);
    }
//=====================REGISTER==================//

    @Test
    public void register_ShouldReturnErrorWithLogin_WhenLoginIsEmpty() {
        Map<String, String> errors = new HashMap<>();
        errors.put("login","Не может быть пустым");
        ErrorProfileDto expected = ErrorProfileDto.builder().errors(errors).build();


        RegisterRequestDto registerRequestDto = getRegisterRequestDto();
        registerRequestDto.setLogin("");

        ErrorProfileDto actual = profileService.register(registerRequestDto);

        assertEquals(expected, actual);
    }

    @Test
    public void register_ShouldReturnErrorWithLogin_WhenLoginSizeLessThan4() {
        Map<String, String> errors = new HashMap<>();
        errors.put("login","Не менее 4 символов");
        ErrorProfileDto expected = ErrorProfileDto.builder().errors(errors).build();


        RegisterRequestDto registerRequestDto = getRegisterRequestDto();
        registerRequestDto.setLogin("use");
        ErrorProfileDto actual = profileService.register(registerRequestDto);

        assertEquals(expected, actual);
    }

    @Test
    public void register_ShouldReturnErrorWithLogin_WhenLoginSizeMoreThan20() {
        Map<String, String> errors = new HashMap<>();
        errors.put("login","Не более 20 символов");
        ErrorProfileDto expected = ErrorProfileDto.builder().errors(errors).build();


        RegisterRequestDto registerRequestDto = getRegisterRequestDto();
        registerRequestDto.setLogin("usuriweouroiweuroweiruweouroiwuee");
        ErrorProfileDto actual = profileService.register(registerRequestDto);

        assertEquals(expected, actual);
    }

    @Test
    public void register_ShouldReturnErrorWithLogin_WhenLoginHaveInvalidSymbols() {
        Map<String, String> errors = new HashMap<>();
        errors.put("login","Присутствуют некорректные символы");
        ErrorProfileDto expected = ErrorProfileDto.builder().errors(errors).build();


        RegisterRequestDto registerRequestDto = getRegisterRequestDto();
        registerRequestDto.setLogin("use^");
        ErrorProfileDto actual = profileService.register(registerRequestDto);

        assertEquals(expected, actual);
    }

    @Test
    public void register_ShouldReturnErrorWithLogin_WhenLoginHasNotLetter() {
        Map<String, String> errors = new HashMap<>();
        errors.put("login","Хотя бы одна буква");
        ErrorProfileDto expected = ErrorProfileDto.builder().errors(errors).build();


        RegisterRequestDto registerRequestDto = getRegisterRequestDto();
        registerRequestDto.setLogin("_._69_._");
        ErrorProfileDto actual = profileService.register(registerRequestDto);

        assertEquals(expected, actual);
    }

    @Test
    public void register_ShouldReturnErrorWithPassword_WhenPasswordIsEmpty() {
        Map<String, String> errors = new HashMap<>();
        errors.put("password","Не может быть пустым");
        errors.put("repeatPassword","Пароли должны совпадать");
        ErrorProfileDto expected = ErrorProfileDto.builder().errors(errors).build();


        RegisterRequestDto registerRequestDto = getRegisterRequestDto();
        registerRequestDto.setPassword("");
        ErrorProfileDto actual = profileService.register(registerRequestDto);

        assertEquals(expected.getErrors(), actual.getErrors());
    }

    @Test
    public void register_ShouldReturnErrorWithPassword_WhenPasswordSizeLessThan4() {
        Map<String, String> errors = new HashMap<>();
        errors.put("password","Не менее 4 символов");
        errors.put("repeatPassword","Пароли должны совпадать");
        ErrorProfileDto expected = ErrorProfileDto.builder().errors(errors).build();


        RegisterRequestDto registerRequestDto = getRegisterRequestDto();
        registerRequestDto.setPassword("use");
        ErrorProfileDto actual = profileService.register(registerRequestDto);

        assertEquals(expected.getErrors(), actual.getErrors());
    }

    @Test
    public void register_ShouldReturnErrorWithPassword_WhenPasswordSizeMoreThan60() {
        Map<String, String> errors = new HashMap<>();
        errors.put("password","Не более 60 символов");
        errors.put("repeatPassword","Пароли должны совпадать");
        ErrorProfileDto expected = ErrorProfileDto.builder().errors(errors).build();


        RegisterRequestDto registerRequestDto = getRegisterRequestDto();
        registerRequestDto.setPassword("usuriweouroiweurowfsdfdfdfeifjdskljfalkjflsjdflkjruweouroiwuee");
        ErrorProfileDto actual = profileService.register(registerRequestDto);

        assertEquals(expected.getErrors(), actual.getErrors());
    }

    @Test
    public void register_ShouldReturnErrorWithPassword_WhenPasswordHaveInvalidSymbols() {
        Map<String, String> errors = new HashMap<>();
        errors.put("password","Присутствуют некорректные символы");
        errors.put("repeatPassword","Пароли должны совпадать");
        ErrorProfileDto expected = ErrorProfileDto.builder().errors(errors).build();


        RegisterRequestDto registerRequestDto = getRegisterRequestDto();
        registerRequestDto.setPassword("user$");
        ErrorProfileDto actual = profileService.register(registerRequestDto);

        assertEquals(expected.getErrors(), actual.getErrors());
    }

    @Test
    public void register_ShouldReturnErrorWithPassword_WhenPasswordHasNotLetter() {
        Map<String, String> errors = new HashMap<>();
        errors.put("password","Хотя бы одна буква");
        errors.put("repeatPassword","Пароли должны совпадать");
        ErrorProfileDto expected = ErrorProfileDto.builder().errors(errors).build();


        RegisterRequestDto registerRequestDto = getRegisterRequestDto();
        registerRequestDto.setPassword("_._69_._");
        ErrorProfileDto actual = profileService.register(registerRequestDto);

        assertEquals(expected.getErrors(), actual.getErrors());
    }

    @Test
    public void register_ShouldReturnErrorWithEmail_WhenEmailIsEmpty() {
        Map<String, String> errors = new HashMap<>();
        errors.put("email","Не может быть пустым");
        ErrorProfileDto expected = ErrorProfileDto.builder().errors(errors).build();


        RegisterRequestDto registerRequestDto = getRegisterRequestDto();
        registerRequestDto.setEmail("");
        ErrorProfileDto actual = profileService.register(registerRequestDto);

        assertEquals(expected.getErrors(), actual.getErrors());
    }

    @Test
    public void register_ShouldReturnErrorWithEmail_WhenEmailIsNotValid() {
        Map<String, String> errors = new HashMap<>();
        errors.put("email","Email некорректно введен");
        ErrorProfileDto expected = ErrorProfileDto.builder().errors(errors).build();


        RegisterRequestDto registerRequestDto = getRegisterRequestDto();
        registerRequestDto.setEmail("lilpip@dip");
        ErrorProfileDto actual = profileService.register(registerRequestDto);

        assertEquals(expected.getErrors(), actual.getErrors());
    }
    @Test
    public void register_ShouldReturnErrorWithEmail_WhenEmailSizeMoreThan150() {
        Map<String, String> errors = new HashMap<>();
        errors.put("email","Не более 150 символов");
        ErrorProfileDto expected = ErrorProfileDto.builder().errors(errors).build();


        RegisterRequestDto registerRequestDto = getRegisterRequestDto();
        registerRequestDto.setEmail("asgdfghasfdhlkjgasfdghafsghdfashgdfhgasfhdfaliljsdkfkjdhlkshjhksdfhgddfsdfsdfdfsgdhgghhdjkshfkshdfkjhsdkjfhksdhfkjkhsdjfhksdhfkjsdfkhshjgfhdhgpip@mail.com");
        ErrorProfileDto actual = profileService.register(registerRequestDto);

        assertEquals(expected.getErrors(), actual.getErrors());
    }
    @Test
    public void register_ShouldReturnErrorWithRepeatPassword_WhenPasswordNotCorrectRepeat() {
        Map<String, String> errors = new HashMap<>();
        errors.put("repeatPassword","Пароли должны совпадать");
        ErrorProfileDto expected = ErrorProfileDto.builder().errors(errors).build();


        RegisterRequestDto registerRequestDto = getRegisterRequestDto();
        registerRequestDto.setRepeatPassword("user1");
        ErrorProfileDto actual = profileService.register(registerRequestDto);

        assertEquals(expected.getErrors(), actual.getErrors());
    }

    @Test
    public void register_ShouldReturnErrorWithLogin_WhenLoginIsExist() {
        Map<String, String> errors = new HashMap<>();
        errors.put("login","Пользователь с таким логином уже найден");
        ErrorProfileDto expected = ErrorProfileDto.builder().errors(errors).build();

        given(roleService.findByName(isA(String.class))).willReturn(getRoles());
        given(profileRepository.findByLogin(isA(String.class))).willReturn(Optional.of(getProfile()));

        RegisterRequestDto registerRequestDto = getRegisterRequestDto();
        ErrorProfileDto actual = profileService.register(registerRequestDto);

        assertEquals(expected.getErrors(), actual.getErrors());
    }

    @Test
    public void register_ShouldReturnErrorProfileDto_WhenAllIsCorrect() {
        Map<String, String> errors = new HashMap<>();
        ErrorProfileDto expected = ErrorProfileDto.builder().profileDto(getProfileResultDto()).errors(errors).build();

        List<Role> roles = getRoles();
        given(roleService.findByName(isA(String.class))).willReturn(roles);
        given(profileRepository.findByLogin(isA(String.class))).willReturn(Optional.empty());
        given(profileRepository.save(isA(Profile.class))).willReturn(getProfile());

        RegisterRequestDto registerRequestDto = getRegisterRequestDto();
        ErrorProfileDto actual = profileService.register(registerRequestDto);

        assertEquals(expected, actual);
    }
}
