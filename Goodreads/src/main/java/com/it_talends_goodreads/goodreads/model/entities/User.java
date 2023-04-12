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

    @Column(name = "user_name")
    private String userName;

    @Column
    private String password;

    @Column(name = "about_me")
    private String aboutMe;

    @Column(name = "link_to_site")
    private String linkToSite;

    @Column
    private String gender;

    @Column(name = "e-mail")
    private String email;
    @OneToMany(mappedBy ="id")
    private List<Shelf> shelves;

    @Column(name = "profile_photo")
    private String profilePhoto;

}
