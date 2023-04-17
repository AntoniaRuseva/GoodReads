package com.it_talends_goodreads.goodreads.model.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users_friends")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@IdClass(FriendID.class)
public class Friend {
    @Id
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Id
    @ManyToOne
    @JoinColumn(name = "friend_id")
    private User friend;

}
