package com.onlywiff.backend.service;

import com.onlywiff.backend.repository.session.Session;
import com.onlywiff.backend.repository.session.SessionRepository;
import com.onlywiff.backend.repository.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

@Service("sessionService")
public class SessionService {

    SessionRepository sessionRepository;
    UserRepository userRepository;

    @Autowired
    public SessionService(SessionRepository sessionRepository, UserRepository userRepository) {
        this.sessionRepository = sessionRepository;
        this.userRepository = userRepository;
    }

    public Mono<Tuple2<Boolean, Session>> checkSession(String sessionToken) {
        return sessionRepository.getSessionBySessionToken(sessionToken).flatMap(session ->
                userRepository.getUserById(session.getUser()).flatMap(user -> {
            if (user.isMfaEnabled()) {
                return Mono.just(!session.isNeedsMFA()).zipWith(Mono.just(session));
            } else {
                return Mono.just(true).zipWith(Mono.just(session));
            }
        })).switchIfEmpty(Mono.just(false).zipWith(Mono.empty()));
    }
}
