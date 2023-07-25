package com.springboot.socialmedia.service.impl;

import com.springboot.socialmedia.dto.LoginDTO;
import com.springboot.socialmedia.dto.SignupDTO;
import com.springboot.socialmedia.model.ERole;
import com.springboot.socialmedia.model.ProfileModel;
import com.springboot.socialmedia.model.RoleModel;
import com.springboot.socialmedia.model.UserModel;
import com.springboot.socialmedia.repository.ProfileRepository;
import com.springboot.socialmedia.repository.RoleRepository;
import com.springboot.socialmedia.repository.UserRepository;
import com.springboot.socialmedia.security.jwt.JwtUtils;
import com.springboot.socialmedia.security.service.UserDetailsImpl;
import com.springboot.socialmedia.service.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class AuthServiceImpl implements AuthService {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private PasswordEncoder encoder;

    @Override
    public String login(LoginDTO loginRequest, HttpServletResponse response) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        // generate jwt
        String jwt = jwtUtils.generateToken(authentication);

        // generate cookie
        ResponseCookie jwtCookie = jwtUtils.generateJWTCookie(userDetails);
        response.addHeader("Set-Cookie", jwtCookie.toString());

        return jwt;
    }

    @Override
    public String signup(SignupDTO signupRequest) {
        UserModel userModel = new UserModel(signupRequest.getFirstname(), signupRequest.getLastname(),
                signupRequest.getEmail(), signupRequest.getPhone(), encoder.encode(signupRequest.getPassword()));
        Set<RoleModel> roles = new HashSet<>();
        Set<String> rolesRequest = signupRequest.getRoles(); // get roles from client

        if (rolesRequest != null) {
            rolesRequest.forEach(role -> {
                if (role.equals("admin")) {
                    RoleModel adminRole = roleRepository.findByCode(ERole.ROLE_ADMIN)
                            .orElseThrow(() -> new RuntimeException("Role is not found!"));
                    roles.add(adminRole);
                }
                if (role.equals("user")) {
                    RoleModel userRole = roleRepository.findByCode(ERole.ROLE_USER)
                            .orElseThrow(() -> new RuntimeException("Role is not found!"));
                    roles.add(userRole);
                }
            });
        } else {
            RoleModel userRole = roleRepository.findByCode(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Role is not found!"));
            roles.add(userRole);
        }

        userModel.setRoles(roles);
        userRepository.saveAndFlush(userModel);

        //save profile
        ProfileModel profile = new ProfileModel(userModel.getId(),
                "https://upload-image-shoppingcart.s3.ap-southeast-1.amazonaws.com/avtdefault.jpg");
        profileRepository.save(profile);
        userModel.setProfile(profile);

        userRepository.save(userModel);
        return "Register Successfully!";
    }

    @Override
    public String logout(HttpServletResponse response) {
        ResponseCookie jwtCookie = jwtUtils.getCleanJwtCookie();
        response.addHeader("Set-Cookie", jwtCookie.toString());
        return "You've been signed out!";
    }
}
