package com.onlywiff.backend.controller.users;

import com.onlywiff.backend.api.GenericObjectResponse;
import com.onlywiff.backend.api.request.UserRegisterRequest;
import com.onlywiff.backend.repository.user.User;
import com.onlywiff.backend.repository.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController()
@RequestMapping(value = "/api/user", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
public class UserController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Mono<GenericObjectResponse<User>> register(@RequestBody UserRegisterRequest userRegisterRequest) {
        return userRepository.existsByUsername(userRegisterRequest.name()).flatMap(exists -> {
            if (exists) {
                return Mono.just(new GenericObjectResponse<>(false, null,"Username already exists"));
            } else {
                return userRepository.existsByEmail(userRegisterRequest.email()).flatMap(exists2 -> {
                    if (exists2) {
                        return Mono.just(new GenericObjectResponse<>(false, null,"Email already exists"));
                    } else {
                        return userRepository.save(new User(userRegisterRequest.name(), userRegisterRequest.displayName(), userRegisterRequest.email(), passwordEncoder.encode(userRegisterRequest.password())))
                                .flatMap(user -> Mono.just(new GenericObjectResponse<>(true, user, "User registered successfully")));
                    }
                });
            }
        });
    }

    public Mono<GenericObjectResponse<?>> login(@RequestBody UserRegisterRequest userRegisterRequest) {
        return userRepository.getUserByUsername(userRegisterRequest.name()).flatMap(user -> {
            if (passwordEncoder.matches(userRegisterRequest.password(), user.getPassword())) {
                return Mono.just(new GenericObjectResponse<>(true, user, "User logged in successfully"));
            } else {
                return Mono.just(new GenericObjectResponse<>(false, null, "Incorrect password"));
            }
        }).switchIfEmpty(Mono.just(new GenericObjectResponse<>(false, null, "User not found")));
    }
}
