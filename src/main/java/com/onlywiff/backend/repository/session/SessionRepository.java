package com.onlywiff.backend.repository.session;

import com.onlywiff.backend.repository.user.User;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface SessionRepository extends R2dbcRepository<Session, String> {

    Mono<Session> getSessionBySessionToken(@Param("sessionToken") String sessionToken);

    Flux<Session> getSessionsByUser(@Param("user") User user);

}
