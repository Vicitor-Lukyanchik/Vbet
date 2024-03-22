package com.vbet.controller;

import com.vbet.dto.AuthenticationRequestDto;
import com.vbet.dto.ErrorProfileDto;
import com.vbet.dto.ProfileDto;
import com.vbet.dto.RegisterRequestDto;
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
@RequestMapping
@RequiredArgsConstructor
public class AuthenticationController {

    private final ProfileService profileService;
    private final SecurityProvider provider;


    @GetMapping("/login")
    public String loginTemplate(Model model, @ModelAttribute("login") AuthenticationRequestDto requestDto,
                                @RequestParam("token") Optional<String> token) {
        return "authentication/login";
    }

    @PostMapping("/login")
    public String login(@Valid @ModelAttribute("login") AuthenticationRequestDto requestDto,
                        RedirectAttributes redirectAttributes) {
        ErrorProfileDto errorProfileDto = profileService.login(requestDto);
        if (errorProfileDto.getErrors().isEmpty()) {
            String token = provider.authenticate(errorProfileDto.getProfileDto());
            return provider.buildUrl("redirect:/hello", token);
        } else {
            redirectAttributes.addFlashAttribute("errorLogin", errorProfileDto.getErrors().get("login"));
            redirectAttributes.addFlashAttribute("errorPassword", errorProfileDto.getErrors().get("password"));
            return "redirect:/login";
        }
    }
    @GetMapping("/register")
    public String registerTemplate(Model model, @ModelAttribute("register") RegisterRequestDto requestDto,
                                @RequestParam("token") Optional<String> token) {
        return "authentication/register";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("register") RegisterRequestDto requestDto,
                        RedirectAttributes redirectAttributes) {
        ErrorProfileDto errorProfileDto = profileService.register(requestDto);
        if (errorProfileDto.getErrors().isEmpty()) {
            String token = provider.authenticate(errorProfileDto.getProfileDto());
            return provider.buildUrl("redirect:/hello", token);
        } else {
            redirectAttributes.addFlashAttribute("errorLogin", errorProfileDto.getErrors().get("login"));
            redirectAttributes.addFlashAttribute("errorEmail", errorProfileDto.getErrors().get("email"));
            redirectAttributes.addFlashAttribute("errorPassword", errorProfileDto.getErrors().get("password"));
            redirectAttributes.addFlashAttribute("errorRepeatPassword", errorProfileDto.getErrors().get("repeatPassword"));
            return "redirect:/register";
        }
    }


    @GetMapping("/hello")
    public String hello(Model model) {
        return "authentication/hello";
    }
}
