package com.onlywiff.backend.repository.user.setting;

import com.onlywiff.backend.repository.user.User;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface UserSettingRepository extends ReactiveCrudRepository<UserSetting, Long> {

    Mono<UserSetting> getUserSettingByUser(@Param("user") User user);

}
