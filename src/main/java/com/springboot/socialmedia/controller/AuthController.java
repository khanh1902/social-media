package com.springboot.socialmedia.controller;

import com.springboot.socialmedia.dto.LoginDTO;
import com.springboot.socialmedia.dto.ResponseObject;
import com.springboot.socialmedia.dto.SignupDTO;
import com.springboot.socialmedia.model.UserModel;
import com.springboot.socialmedia.service.AuthService;
import com.springboot.socialmedia.service.RoleService;
import com.springboot.socialmedia.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private UserService userService;

    @Autowired
    private AuthService authService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /* Json signupRequest
        {
            "firstname": "Van",
            "lastname": "Khanh",
            "phone": "0948456075",
            "email":"khanh1@gmail.com",
            "password": "123456",
            "confirmPassword": "123456",
            "roles":["admin"]
        }
     */
    @PostMapping("/signup")
    public ResponseEntity<ResponseObject> signup(@RequestBody SignupDTO signupRequest){
        if(userService.existsByEmail(signupRequest.getEmail()))
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                    new ResponseObject("FAILED", "Email really Exists!", null)
            );
        if(!signupRequest.getPassword().equals(signupRequest.getConfirmPassword())){
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                    new ResponseObject("FAILED", "Confirm Password Not Match!", null)
            );
        } else {
            return ResponseEntity.status(HttpStatus.CREATED).body(
                    new ResponseObject("OK", authService.signup(signupRequest), null)
            );
        }
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseObject> login(@RequestBody LoginDTO loginRequest, HttpServletResponse response){

        UserModel userModel = userService.findByEmail(loginRequest.getEmail());
        if(userModel == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject("FAILED", "Email Does Not Exists!", null)
            );
        }
        if(!passwordEncoder.matches(loginRequest.getPassword(), userModel.getPassword())){
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                    new ResponseObject("FAILED", "Password Does Not Match!", null)
            );
        }
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK", "Login Successfully!", authService.login(loginRequest, response))
        );

    }

    @PostMapping("/logout")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<ResponseObject> logout(HttpServletResponse response){
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK", authService.logout(response), null)
        );
    }
}
