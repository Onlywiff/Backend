package com.onlywiff.backend.repository.post;

import com.onlywiff.backend.repository.user.User;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PostRepository extends ReactiveCrudRepository<Post, Long> {

    Mono<Post> getPostById(@Param("id") long id);

    Flux<Post> getPostsByUser(@Param("user") User user);

    Mono<Boolean> existsPostById(@Param("id") long id);

}
