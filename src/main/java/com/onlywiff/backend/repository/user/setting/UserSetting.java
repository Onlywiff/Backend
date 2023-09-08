package com.onlywiff.backend.repository.user.setting;

import com.onlywiff.backend.repository.user.User;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
public class UserSetting {

    @Id
    @NonNull
    @OneToOne
    @JoinColumn(name="id", nullable=false, updatable=false)
    private User user;

    private boolean notifyArson;

}
