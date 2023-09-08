package com.onlywiff.backend.repository.post;

import com.onlywiff.backend.repository.user.User;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface PostRepository extends R2dbcRepository<Post, Long> {

    Mono<Post> getPostById(@Param("id") long id);

    Flux<Post> getPostsByPoster(@Param("poster") User user);

    Mono<Boolean> existsPostById(@Param("id") long id);

}
