package com.onlywiff.backend.repository.user.setting;

import com.onlywiff.backend.repository.user.User;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
public class UserSetting {

    @Id
    @OneToOne
    @JoinColumn(name="user", nullable=false, updatable=false)
    private User user;

    private boolean notifyArson;

}
