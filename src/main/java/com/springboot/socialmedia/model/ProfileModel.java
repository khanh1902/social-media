package com.springboot.socialmedia.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "profile")
@Getter
@Setter
@NoArgsConstructor
public class ProfileModel {
    @Id
    private Long id;
    private String sex;
    private String address;
    private String hobby;
    @Column(name = "date_of_birth")
    private String dateOfBirth;
    @Column(name = "image_url",columnDefinition = "Text")
    private String imageUrl;

    public ProfileModel(Long id, String imageUrl) {
        this.id = id;
        this.imageUrl = imageUrl;
    }
}
