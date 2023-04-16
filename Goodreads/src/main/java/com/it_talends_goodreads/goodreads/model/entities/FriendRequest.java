package com.it_talends_goodreads.goodreads.model.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FriendRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private User requester;

    @ManyToOne(fetch = FetchType.LAZY)
    private User receiver;

    private boolean accepted;

    private boolean rejected;
    public boolean isAccepted(){
        return this.accepted;
    }
    public boolean isRejected(){
        return this.rejected;
    }
}
