package com.springboot.socialmedia.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SignupDTO {
    private String firstname;
    private String lastname;
    private String phone;
    private String email;
    private String password;
    private String confirmPassword;
    private Set<String> roles;
}
