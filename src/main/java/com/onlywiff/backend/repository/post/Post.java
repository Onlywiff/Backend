package com.onlywiff.backend.repository.post;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.onlywiff.backend.repository.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.domain.Persistable;
import org.springframework.data.r2dbc.config.EnableR2dbcAuditing;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.sql.Timestamp;
import java.util.Set;

@Table
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EnableR2dbcAuditing
public class Post implements Persistable<Long> {

    @Id
    @GeneratedValue
    private Long id;

    @Column(value = "poster")
    @ManyToOne(optional = false)
    @JoinColumn(name="id", nullable=false, updatable=false)
    private User poster;

    public String content;

    @ManyToOne
    @Column(value = "replyTo")
    @JoinColumn(name="id", updatable=false)
    Post replyTo;

    @OneToMany(mappedBy = "Post", fetch = FetchType.LAZY)
    Set<Post> comments;

    boolean isPrivate;

    @JsonIgnore
    boolean isDeleted;

    @JsonIgnore
    @Override
    public boolean isNew() {
        return id == null || id == 0;
    }
}
