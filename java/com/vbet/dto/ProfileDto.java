package com.vbet.dto;

import com.vbet.entity.BalanceTransaction;
import com.vbet.entity.ProfileBet;
import com.vbet.entity.Role;
import lombok.Builder;
import lombok.Data;

import javax.persistence.OneToMany;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
public class ProfileDto {

    private Long id;

    @Size(min = 4, max = 60, message = "Login should be more then 6 and less than 60")
    private String login;

    @Size(min = 4, max = 60, message = "Password should be more then 6 and less than 60")
    private String password;

    @Size(min = 2, max = 150, message = "Firstname should be more than 2 and less than 150")
    @Pattern(regexp = "^[A-Z][a-z0-9_-]{3,99}$", message = "First letter in firstname should be uppercase")
    private String firstname;

    @Size(min = 2, max = 150, message = "Lastname should be more than 2 and less than 150")
    @Pattern(regexp = "^[A-Z][a-z0-9_-]{3,99}$", message = "First letter in lastname should be uppercase")
    private String lastname;

    @Size(max = 150, message = "Email should be less than 150")
    @Email(message = "Email should be valid")
    private String email;

    @DecimalMin(value = "0.10", message = "Profile bet amount can't be less than 0.10")
    @Digits(integer=8, fraction=2, message = "Profile bet amount digits more than 8 and fraction more than 2")
    private BigDecimal balance;

    private LocalDate dob;

    private List<Role> roles;

    private List<BalanceTransaction> transactions;

    private List<ProfileBet> profileBets;

    private String message;

}