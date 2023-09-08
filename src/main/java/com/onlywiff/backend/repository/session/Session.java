package com.onlywiff.backend.repository.session;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.onlywiff.backend.repository.user.User;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.sql.Timestamp;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
public class Session {

    public Session(String sessionToken, User user, boolean needsMFA) {
        this(sessionToken, user);
        this.needsMFA = needsMFA;
    }

    @Id
    @NonNull
    String sessionToken;

    @NonNull
    @ManyToOne(optional = false)
    @JoinColumn(name="user", nullable=false, updatable=false)
    private User user;

    private boolean needsMFA;

    @CreatedDate
    public Timestamp created;

    @JsonIgnore
    @LastModifiedDate
    public Timestamp lastModified;

}
