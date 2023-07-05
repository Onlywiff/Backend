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

    private boolean accountVerified;

    private int failedLoginAttempts;

    private boolean loginDisabled;

    @JsonIgnore
    private String mfaSecret;

    private boolean mfaEnabled;

    private boolean isPublic;
}
