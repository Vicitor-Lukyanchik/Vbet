package com.vbet.controller;

import com.vbet.dto.AuthenticationRequestDto;
import com.vbet.dto.ErrorProfileDto;
import com.vbet.dto.ProfileDto;
import com.vbet.security.SecurityProvider;
import com.vbet.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.Optional;

@Controller
@RequestMapping(value = "/login")
@RequiredArgsConstructor
public class AuthenticationController {

    private final ProfileService profileService;
    private final SecurityProvider provider;


    @GetMapping()
    public String loginTemplate(Model model, @ModelAttribute("login") AuthenticationRequestDto requestDto,
                                @RequestParam("token") Optional<String> token) {
        //token.ifPresent(provider::deauthenticate);
        return "authentication/login";
    }

    @PostMapping()
    public String login(@Valid @ModelAttribute("login") AuthenticationRequestDto requestDto,
                        RedirectAttributes redirectAttributes) {
        ErrorProfileDto errorProfileDto = profileService.login(requestDto);
        if (errorProfileDto.getErrors().isEmpty()) {
            String token = provider.authenticate(errorProfileDto.getProfileDto());
            return provider.buildUrl("redirect:/login/hello", token);
        } else {
            redirectAttributes.addFlashAttribute("errorLogin", errorProfileDto.getErrors().get("login"));
            redirectAttributes.addFlashAttribute("errorPassword", errorProfileDto.getErrors().get("password"));
            return "redirect:/login";
        }
    }


    @GetMapping("/hello")
    public String hello(Model model) {
        return "authentication/hello";
    }
}
