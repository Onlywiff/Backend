package com.onlywiff.backend.repository.post;

import com.onlywiff.backend.repository.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Post {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name="poster", nullable=false, updatable=false)
    private User user;

    @ManyToOne()
    @JoinColumn(name="replyTo", updatable=false)
    Post replyTo;

    @OneToMany(mappedBy = "replyTo")
    Set<Post> comments;
}
