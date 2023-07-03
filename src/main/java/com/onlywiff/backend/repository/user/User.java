package com.onlywiff.backend.repository.user;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue
    private Long id;
    private String displayName;
    private String username;
    private String password;

    @Column(unique = true)
    private String email;
    private boolean accountVerified;
    private int failedLoginAttempts;
    private boolean loginDisabled;
    private String mfaSecret;
    private boolean mfaEnabled;
    private boolean isPublic;
}
