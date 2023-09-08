package com.onlywiff.backend.repository.upload;

import com.onlywiff.backend.repository.post.Post;
import com.onlywiff.backend.repository.user.User;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UploadRepository extends ReactiveCrudRepository<Upload, Long> {

    Mono<Upload> getUploadById(@Param("id") long id);

    Flux<Upload> getUploadsByUser(@Param("user") User user);

    Mono<Boolean> existsUploadById(@Param("id") long id);

}
