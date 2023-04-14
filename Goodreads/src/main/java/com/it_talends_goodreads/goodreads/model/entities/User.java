package com.it_talends_goodreads.goodreads.model.entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @OneToMany(mappedBy = "user")
    private List<Challenge> challenges;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "user_name",nullable = false)
    private String userName;

    @Column(nullable = false)
    private String password;

    @Column(name = "about_me",columnDefinition = "TEXT")
    private String aboutMe;

    @Column(name = "link_to_site")
    private String linkToSite;

    @Column
    private String gender;

    @Column(name = "e_mail",nullable = false)
    private String email;
    @OneToMany(mappedBy ="id")
    private List<Shelf> shelves;

    @Column(name = "profile_photo")
    private String profilePhoto;

    @ManyToMany(mappedBy = "likedBy")
    private List<Review> likedReviews;

}
