package com.springboot.socialmedia.controller;

import com.springboot.socialmedia.dto.ResponseObject;
import com.springboot.socialmedia.service.UserPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/user/post")
public class UserPostController {
    @Autowired
    private UserPostService userPostService;

    @PostMapping()
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<ResponseObject> AddUserPost(@RequestParam(name = "message") String message,
                                                      @RequestParam(name = "files") MultipartFile[] multipartFiles){
        return ResponseEntity.status(HttpStatus.CREATED).body(
                new ResponseObject("OK", "Post Successfully!", userPostService.post(message, multipartFiles))
        );
    }

    @PutMapping("/update")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<ResponseObject> updateUserPost(@RequestParam(name = "id") Long id,
                                                         @RequestParam(name = "message") String message,
                                                         @RequestParam(name = "files") MultipartFile[] multipartFiles) {
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK", "Update Post Successfully!", userPostService.updatePost(id, message, multipartFiles))
        );
    }
}
