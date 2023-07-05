package com.onlywiff.backend.repository.post;

import com.onlywiff.backend.repository.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.sql.Timestamp;
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

    public String content;

    @CreatedDate
    public Timestamp created;

    @LastModifiedDate
    public Timestamp lastModified;

    @ManyToOne()
    @JoinColumn(name="replyTo", updatable=false)
    Post replyTo;

    @OneToMany(mappedBy = "Post", fetch = FetchType.LAZY)
    Set<Post> comments;
}
