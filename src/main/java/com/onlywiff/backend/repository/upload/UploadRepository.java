package com.onlywiff.backend.repository.upload;

import com.onlywiff.backend.repository.user.User;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface UploadRepository extends R2dbcRepository<Upload, Long> {

    Mono<Upload> getUploadById(@Param("id") long id);

    Flux<Upload> getUploadsByUploader(@Param("uploader") User user);

    Mono<Boolean> existsUploadById(@Param("id") long id);

}
