package com.onlywiff.backend.repository.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
public class User {

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

    private boolean isPublic;

    public void enableMFA(String secret) {
        mfaSecret = secret;
        mfaEnabled = true;
    }
}
