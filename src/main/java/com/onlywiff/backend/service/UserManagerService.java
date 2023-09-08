package com.onlywiff.backend.service;

import com.onlywiff.backend.api.GenericObjectResponse;
import com.onlywiff.backend.api.GenericResponse;
import com.onlywiff.backend.api.request.UserMFARequest;
import com.onlywiff.backend.repository.session.Session;
import com.onlywiff.backend.repository.session.SessionRepository;
import com.onlywiff.backend.repository.user.User;
import com.onlywiff.backend.repository.user.UserRepository;
import com.onlywiff.backend.repository.user.setting.UserSetting;
import com.onlywiff.backend.repository.user.setting.UserSettingRepository;
import com.onlywiff.backend.service.mfa.MFAManagerService;
import com.onlywiff.backend.utility.RandomUtils;
import dev.samstevens.totp.exceptions.QrGenerationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.HashMap;

@Service("userManagerService")
public class UserManagerService {

    UserRepository userRepository;
    SessionService sessionService;
    PasswordEncoder passwordEncoder;
    SessionRepository sessionRepository;
    MFAManagerService mfaManagerService;
    UserSettingRepository userSettingRepository;

    @Autowired
    public UserManagerService(UserRepository userRepository,
                              UserSettingRepository userSettingRepository,
                              SessionRepository sessionRepository,
                              MFAManagerService mfaManagerService,
                              PasswordEncoder passwordEncoder,
                              SessionService sessionService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.sessionRepository = sessionRepository;
        this.mfaManagerService = mfaManagerService;
        this.userSettingRepository = userSettingRepository;
        this.sessionService = sessionService;
    }

    HashMap<Long, String> emailVerification = new HashMap<>();
    HashMap<Long, String> mfaSecretMap = new HashMap<>();

    public Mono<GenericObjectResponse<User>> getUser(String sessionToken, String userId) {
        if (sessionToken == null || userId == null) {
            return Mono.just(new GenericObjectResponse<>(false, null, "Missing Parameters"));
        }

        long id;

        try {
            id = Long.parseLong(userId);
        } catch (Exception e) {
            return Mono.just(new GenericObjectResponse<>(false, null, "Missing Parameters"));
        }

        return sessionService.checkSession(sessionToken).flatMap(tuple -> {
            if (tuple.getT1()) {
                return userRepository.getUserById(tuple.getT2().getUser()).flatMap(user -> {
                    if (user.getId() == id) {
                        return Mono.just(new GenericObjectResponse<User>(true, user, "Success"));
                    } else {
                        return userRepository.getUserById(id).flatMap(x -> {
                            x.setEmail("");
                            return Mono.just(new GenericObjectResponse<User>(true, x, "Success"));
                        });
                    }
                }).switchIfEmpty(Mono.just(new GenericObjectResponse<User>(false, null, "User not found")));
            } else {
                return Mono.just(new GenericObjectResponse<User>(false, null, "Needs MFA Login"));
            }
        }).switchIfEmpty(Mono.just(new GenericObjectResponse<User>(false, null, "Session not found")));
    }

    public Mono<GenericObjectResponse<Session>> test(String username, String displayName, String email, String password) {
        if (username == null || displayName == null || email == null || password == null) {
            return Mono.just(new GenericObjectResponse<>(false, null, "Missing Parameters"));
        }
        return null;
    }

    public Mono<GenericObjectResponse<Session>> register(String username, String displayName, String email, String password) {
        if (username == null || displayName == null || email == null || password == null) {
            return Mono.just(new GenericObjectResponse<>(false, null, "Missing Parameters"));
        }

        return userRepository.existsByUsername(username).flatMap(exists -> {
            if (exists) {
                return Mono.just(new GenericObjectResponse<Session>(false, null, "Username already exists"));
            } else {
                return userRepository.existsByEmail(email).flatMap(exists2 -> {
                    if (exists2) {
                        return Mono.just(new GenericObjectResponse<Session>(false, null, "Email already exists"));
                    } else {
                        return userRepository.save(new User(displayName, username, passwordEncoder.encode(password), email))
                                .flatMap(user ->
                                        sessionRepository.save(new Session(RandomUtils.getRandomBase64String(128), user, false))
                                                .flatMap(session -> Mono.just(new GenericObjectResponse<Session>(true, session, "User registered successfully"))
                                                ));
                    }
                });
            }
        }).onErrorReturn(new GenericObjectResponse<Session>(false, null, "Failed"));
    }

    public Mono<GenericObjectResponse<Session>> login(String username, String password) {
        if (username == null || password == null) {
            return Mono.just(new GenericObjectResponse<>(false, null, "Missing Parameters"));
        }

        return userRepository.getUserByUsername(username).flatMap(user -> {
                    if (user.isLoginDisabled()) {
                        return Mono.just(new GenericObjectResponse<Session>(false, null, "Login disabled"));
                    } else {
                        if (passwordEncoder.matches(password, user.getPassword())) {
                            user.setFailedLoginAttempts(0);
                            return userRepository.save(user).flatMap(save ->
                                    sessionRepository.save(new Session(RandomUtils.getRandomBase64String(128), save, save.isMfaEnabled()))
                                            .flatMap(session -> Mono.just(new GenericObjectResponse<>(true, session, "User login successful"))));
                        } else {
                            if (user.getFailedLoginAttempts() >= 3) {
                                user.setLoginDisabled(true);
                            } else {
                                user.setFailedLoginAttempts(user.getFailedLoginAttempts() + 1);
                            }

                            return userRepository.save(user).flatMap(save ->
                                    Mono.just(new GenericObjectResponse<Session>(false, null, save.isLoginDisabled() ? "Failed to often" : "Incorrect password")));
                        }
                    }
                }).switchIfEmpty(Mono.just(new GenericObjectResponse<>(false, null, "User not found")))
                .onErrorReturn(new GenericObjectResponse<>(false, null, "Failed"));
    }

    public Mono<GenericObjectResponse<Session>> mfaLogin(String sessionToken, String code) {
        if (sessionToken == null || code == null) {
            return Mono.just(new GenericObjectResponse<>(false, null, "Missing Parameters"));
        }

        return sessionRepository.getSessionBySessionToken(sessionToken).flatMap(session ->
                userRepository.getUserById(session.getUser()).flatMap(user -> {
                    if (user.isMfaEnabled() && session.isNeedsMFA()) {
                        return mfaManagerService.verifyCode(code, user.getMfaSecret())
                                .flatMap(mfaResult -> {
                                    if (mfaResult) {
                                        session.setNeedsMFA(false);
                                        session.setNew(false);
                                        return sessionRepository.save(session)
                                                .flatMap(x -> Mono.just(new GenericObjectResponse<Session>(true, x, "User passed MFA successfully")));
                                    } else {
                                        return sessionRepository.delete(session)
                                                .then(Mono.defer(() -> Mono.just(new GenericObjectResponse<Session>(false, null, "User failed MFA"))));
                                    }
                                });
                    } else {
                        return Mono.just(new GenericObjectResponse<Session>(true, session,
                                !user.isMfaEnabled() ? "User doesn't have MFA enabled, skipping check!" :
                                        "Session doesn't need MFA"));
                    }
                })).switchIfEmpty(Mono.just(new GenericObjectResponse<Session>(false, null, "No Session")));
    }

    public Mono<UserMFARequest> mfaRequest(String sessionToken) {
        if (sessionToken == null) {
            return Mono.just(new UserMFARequest(false, null, null, "Missing Parameters"));
        }

        return sessionService.checkSession(sessionToken).flatMap(tuple -> {
            if (tuple.getT1()) {
                return userRepository.getUserById(tuple.getT2().getUser()).flatMap(user -> {

                    if (!user.isMfaEnabled()) {
                        if (mfaSecretMap.containsKey(user.getId())) {
                            return Mono.just(new UserMFARequest(false, null, null, "MFA setup already started"));
                        }

                        String mfaSecret = mfaManagerService.generateSecret();

                        try {
                            return mfaManagerService.getQRCode(mfaSecret, user.getEmail())
                                    .flatMap(x -> {
                                        mfaSecretMap.put(user.getId(), mfaSecret);
                                        return Mono.just(new UserMFARequest(true, x, mfaSecret, "Success"));
                                    });
                        } catch (QrGenerationException e) {
                            return Mono.error(new RuntimeException(e));
                        }
                    } else {
                        return Mono.just(new UserMFARequest(false, null, null, "MFA is already set"));
                    }
                });
            } else {
                // This should never be reached? Unless someone tries to do shit with the API instead of using our clients.
                return Mono.just(new UserMFARequest(false, null, null, "Needs MFA Login"));
            }
        }).onErrorReturn(new UserMFARequest(false, null, null, "Failed"));
    }

    public Mono<GenericResponse> mfaActivate(String sessionToken, String code) {
        if (sessionToken == null || code == null) {
            return Mono.just(new GenericResponse(false, "Missing Parameters"));
        }

        return sessionService.checkSession(sessionToken).flatMap(tuple -> {
            if (tuple.getT1()) {
                return userRepository.getUserById(tuple.getT2().getUser()).flatMap(user -> {

                    if (!mfaSecretMap.containsKey(user.getId())) {
                        return Mono.just(new GenericResponse(false, "No setup has been started"));
                    }

                    String currentSecret = mfaSecretMap.get(user.getId());

                    return mfaManagerService.verifyCode(code, currentSecret)
                            .flatMap(result -> {
                                if (result) {
                                    user.enableMFA(currentSecret);
                                    return userRepository.save(user)
                                            .then(Mono.just(new GenericResponse(true, "Enabled MFA")));
                                } else {
                                    return userRepository.save(user)
                                            .then(Mono.just(new GenericResponse(false, "Invalid MFA code")));
                                }
                            });
                });
            } else {
                return Mono.just(new GenericResponse(false, "Needs MFA Login"));
            }
        });
    }

    public Mono<GenericResponse> mfaDisable(String sessionToken, String code) {
        if (sessionToken == null || code == null) {
            return Mono.just(new GenericResponse(false, "Missing Parameters"));
        }

        return sessionService.checkSession(sessionToken).flatMap(tuple -> {
            if (tuple.getT1()) {
                return userRepository.getUserById(tuple.getT2().getUser()).flatMap(user -> {

                    return mfaManagerService.verifyCode(code, user.getMfaSecret())
                            .flatMap(result -> {
                                if (result) {
                                    user.setMfaEnabled(false);
                                    return userRepository.save(user)
                                            .then(Mono.just(new GenericResponse(true, "Disabled MFA")));
                                } else {
                                    return userRepository.save(user)
                                            .then(Mono.just(new GenericResponse(false, "Invalid MFA code")));
                                }
                            });
                });
            } else {
                return Mono.just(new GenericResponse(false, "Needs MFA Login"));
            }
        });
    }

    public Mono<GenericResponse> verifyEmail(String sessionToken, String verificationCode) {
        if (sessionToken == null || verificationCode == null) {
            return Mono.just(new GenericResponse(false, "Missing Parameters"));
        }

        return sessionRepository.getSessionBySessionToken(sessionToken)
                .flatMap(session -> userRepository.getUserById(session.getUser()).flatMap(user -> {
                    if (!user.isEmailVerified()) {
                        long userId = user.getId();

                        if (emailVerification.containsKey(userId)) {
                            String code = emailVerification.get(userId);

                            if (code.equals(verificationCode)) {
                                user.setEmailVerified(true);
                                return userRepository.save(user)
                                        .then(Mono.just(new GenericResponse(true, "Email verified")));
                            } else {
                                return Mono.just(new GenericResponse(false, "Email code invalid"));
                            }
                        } else {
                            return Mono.just(new GenericResponse(false, "Email code expired"));
                        }
                    } else {
                        return Mono.just(new GenericResponse(false, "Email already verified"));
                    }
                })).switchIfEmpty(Mono.just(new GenericResponse(false, "No Session")));
    }

    public Mono<UserSetting> getSettings(User user) {
        return userSettingRepository.getUserSettingByUser(user);
    }
}
