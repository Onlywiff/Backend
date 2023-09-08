package com.onlywiff.backend.repository.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.domain.Persistable;
import org.springframework.data.r2dbc.config.EnableR2dbcAuditing;
import org.springframework.data.relational.core.mapping.Table;

@Table
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EnableR2dbcAuditing
@RequiredArgsConstructor
public class User implements Persistable<Long> {

    @Id
    @JsonIgnore
    @GeneratedValue
    private Long id;

    @NonNull
    private String displayName;

    @NonNull
    private String username;

    @NonNull
    @JsonIgnore
    private String password;

    private String bio;

    @NonNull
    @Column(unique = true)
    private String email;

    @JsonIgnore
    private boolean emailVerified;

    private UserTyp accountVerification = UserTyp.NON_VERIFIED;

    @JsonIgnore
    private int failedLoginAttempts;

    @JsonIgnore
    private boolean loginDisabled;

    @JsonIgnore
    private String mfaSecret;

    @JsonIgnore
    private boolean mfaEnabled;

    private boolean isPublic = true;

    public void enableMFA(String secret) {
        mfaSecret = secret;
        mfaEnabled = true;
    }

    @JsonIgnore
    @Override
    public boolean isNew() {
        return id == null || id == 0;
    }
}
