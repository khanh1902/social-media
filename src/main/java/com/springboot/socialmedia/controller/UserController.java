package com.springboot.socialmedia.controller;

import com.springboot.socialmedia.dto.ResponseObject;
import com.springboot.socialmedia.model.ProfileModel;
import com.springboot.socialmedia.service.ProfileService;
import com.springboot.socialmedia.service.UserPostService;
import com.springboot.socialmedia.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private ProfileService profileService;



    @GetMapping("/get-me")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<ResponseObject> getInforUser(@RequestHeader(name = "Authorization") String jwt){
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK", "Get Information User Successfully!", userService.inforUser(jwt.substring(7)))
        );
    }

    @PutMapping("/update-avt")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<ResponseObject> updateAvatar(@RequestParam(name = "id") Long id,
                                                       @RequestParam(name = "file") MultipartFile multipartFile){
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK", "Update Successfully!", profileService.updateAvatar(id, multipartFile))
        );
    }

    @PutMapping("/update-profile")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<ResponseObject> updateProfile(@RequestParam(name = "id") Long id,
                                                        @RequestBody ProfileModel profile){
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK", "Update Successfully!", profileService.updateInfor(id, profile))
        );
    }


}
