package com.onlywiff.backend.repository.user;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.jboss.aerogear.security.otp.api.Base32;

@Getter
@Setter
@Entity
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue
    private Long id;
    private String displayName;
    private String username;
    private String password;
    private String email;
    private String mfaSecret;
    private boolean mfaEnabled;
    private boolean isPublic;

    public User() {
        mfaSecret = Base32.random();
    }
}
