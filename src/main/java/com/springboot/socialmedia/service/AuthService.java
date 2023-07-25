package com.springboot.socialmedia.service;

import com.springboot.socialmedia.dto.LoginDTO;
import com.springboot.socialmedia.dto.SignupDTO;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;


@Service
public interface AuthService {
    String login(LoginDTO loginRequest, HttpServletResponse response);
    String signup(SignupDTO signupRequest);
    String logout(HttpServletResponse response);
}
