package com.springboot.socialmedia.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InforUser {
    private Long id;
    private String firstname;
    private String lastname;
    private String email;
    private String imageUrl;
    private Set<String> roles;
}
