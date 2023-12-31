package com.onlywiff.backend.repository.user;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface UserRepository extends R2dbcRepository<User, Long> {

    Mono<User> getUserById(@Param("id") long id);
    Mono<User> getUserByUsername(@Param("username") String username);
    Mono<User> getUserByEmail(@Param("email") String email);
    Flux<User> getUsersByDisplayName(@Param("displayName") String displayName);

    Mono<Boolean> existsByUsername(@Param("username") String username);

    Mono<Boolean> existsByEmail(@Param("email") String email);
}
