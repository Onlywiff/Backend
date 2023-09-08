package com.onlywiff.backend.repository.session;

import com.onlywiff.backend.repository.post.Post;
import com.onlywiff.backend.repository.user.User;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface SessionRepository extends ReactiveCrudRepository<Session, String> {

    Mono<Session> getSessionBySessionToken(@Param("sessionToken") String sessionToken);

    Flux<Session> getSessionsByUser(@Param("user") User user);

}
