package com.it_talends_goodreads.goodreads.model.entities;

import jakarta.persistence.Embeddable;

import java.io.Serializable;

@Embeddable
public class FriendID implements Serializable {
    private User user;
    private User friend;

    public FriendID(User user, User friend){
        this.user = user;
        this.friend = friend;
    }

    public FriendID() {
    }
}
