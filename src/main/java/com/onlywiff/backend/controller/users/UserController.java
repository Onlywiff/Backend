package com.onlywiff.backend.controller.users;

import com.onlywiff.backend.api.GenericObjectResponse;
import com.onlywiff.backend.api.GenericResponse;
import com.onlywiff.backend.api.request.GenericValueRequest;
import com.onlywiff.backend.api.request.UserLoginRequest;
import com.onlywiff.backend.api.request.UserMFARequest;
import com.onlywiff.backend.api.request.UserRegisterRequest;
import com.onlywiff.backend.repository.session.Session;
import com.onlywiff.backend.repository.user.User;
import com.onlywiff.backend.repository.user.UserRepository;
import com.onlywiff.backend.service.SessionService;
import com.onlywiff.backend.service.UserManagerService;
import com.onlywiff.backend.service.mfa.MFAManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController()
@RequestMapping(value = "/api/user", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
public class UserController {

    final SessionService sessionService;
    final MFAManagerService mfaManagerService;
    private final UserManagerService userManagerService;

    @Autowired
    public UserController(SessionService sessionService,
                          MFAManagerService mfaManagerService,
                          UserManagerService userManagerService) {
        this.sessionService = sessionService;
        this.mfaManagerService = mfaManagerService;
        this.userManagerService = userManagerService;
    }

    @RequestMapping(value = "/register")
    public Mono<GenericObjectResponse<Session>> register(@RequestBody UserRegisterRequest userRegisterRequest) {
        return userManagerService.register(userRegisterRequest.name(),
                userRegisterRequest.displayName(), userRegisterRequest.email(), userRegisterRequest.password());
    }

    @RequestMapping(value = "/login")
    public Mono<GenericObjectResponse<Session>> login(@RequestBody UserLoginRequest userLoginRequest) {
        return userManagerService.login(userLoginRequest.name(), userLoginRequest.password());
    }

    @RequestMapping(value = "/mfa/login")
    public Mono<GenericObjectResponse<Session>> mfaLogin(@RequestHeader(name = "Authorization") String sessionToken, @RequestBody GenericValueRequest genericValueRequest) {
        return userManagerService.mfaLogin(sessionToken, genericValueRequest.value());
    }

    @RequestMapping(value = "/verify")
    public Mono<GenericResponse> verifyEmail(@RequestHeader(name = "Authorization") String sessionToken, @RequestBody GenericValueRequest genericValueRequest) {
        return userManagerService.verifyEmail(sessionToken, genericValueRequest.value());
    }

    @RequestMapping(value = "/mfa/activate")
    public Mono<GenericResponse> mfaActivate(@RequestHeader(name = "Authorization") String sessionToken, @RequestBody GenericValueRequest genericValueRequest) {
        return userManagerService.mfaActivate(sessionToken, genericValueRequest.value());
    }

    @RequestMapping(value = "/mfa/request")
    public Mono<UserMFARequest> mfaRequest(@RequestHeader(name = "Authorization") String sessionToken) {
        return userManagerService.mfaRequest(sessionToken);
    }

    @RequestMapping(value = "/mfa/deactivate")
    public Mono<GenericResponse> mfaDeactivate(@RequestHeader(name = "Authorization") String sessionToken, @RequestBody GenericValueRequest genericValueRequest) {
        return userManagerService.mfaDisable(sessionToken, genericValueRequest.value());
    }
}
