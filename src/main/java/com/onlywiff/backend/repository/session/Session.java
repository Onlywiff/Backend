package com.onlywiff.backend.repository.session;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.onlywiff.backend.repository.user.User;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.domain.Persistable;
import org.springframework.data.r2dbc.config.EnableR2dbcAuditing;
import org.springframework.data.relational.core.mapping.Table;

import java.sql.Timestamp;

@Table
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EnableR2dbcAuditing
@RequiredArgsConstructor
public class Session implements Persistable<String> {

    public Session(String sessionToken, User user, boolean needsMFA) {
        this(sessionToken, user.getId());
        this.needsMFA = needsMFA;
    }

    @Id
    @NonNull
    String sessionToken;

    @NonNull
    private long user;

    private boolean needsMFA;

    @Override
    @JsonIgnore
    public String getId() {
        return sessionToken;
    }

    @JsonIgnore
    boolean isNew = true;

    @Override
    @JsonIgnore
    public boolean isNew() {
        return isNew;
    }
}
