package com.onlywiff.backend.repository.user.setting;

import com.onlywiff.backend.repository.user.User;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface UserSettingRepository extends R2dbcRepository<UserSetting, Long> {

    Mono<UserSetting> getUserSettingByUser(@Param("user") User user);

}
