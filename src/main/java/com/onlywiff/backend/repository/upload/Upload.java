package com.onlywiff.backend.repository.upload;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.onlywiff.backend.repository.converter.ByteToBlobAttributeConverter;
import com.onlywiff.backend.repository.user.User;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.domain.Persistable;
import org.springframework.data.r2dbc.config.EnableR2dbcAuditing;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EnableR2dbcAuditing
@RequiredArgsConstructor
public class Upload implements Persistable<Long> {

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
    @Column(value = "uploader")
    @ManyToOne(optional = false)
    @JoinColumn(name="id", nullable=false, updatable=false)
    private User uploader;

    @Convert(converter = ByteToBlobAttributeConverter.class)
    private byte[] content;

    boolean isPrivate;

    @JsonIgnore
    @Override
    public boolean isNew() {
        return id == null || id == 0;
    }
}
