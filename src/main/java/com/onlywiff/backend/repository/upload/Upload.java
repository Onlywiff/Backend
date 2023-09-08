package com.onlywiff.backend.repository.upload;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.onlywiff.backend.repository.converter.ByteToBlobAttributeConverter;
import com.onlywiff.backend.repository.user.User;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
public class Upload {

    public Upload(String name, User user, byte[] content) {
        this(name, user);
        this.content = content;
    }

    @Id
    @JsonIgnore
    @GeneratedValue
    private Long id;

    @NonNull
    private String name;

    @NonNull
    @ManyToOne(optional = false)
    @JoinColumn(name="uploader", nullable=false, updatable=false)
    private User user;

    @Convert(converter = ByteToBlobAttributeConverter.class)
    private byte[] content;

    boolean isPrivate;
}
