package com.onlywiff.backend.repository.upload;

import com.onlywiff.backend.repository.converter.ByteToBlobAttributeConverter;
import com.onlywiff.backend.repository.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Upload {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    @ManyToOne(optional = false)
    @JoinColumn(name="uploader", nullable=false, updatable=false)
    private User user;

    @Convert(converter = ByteToBlobAttributeConverter.class)
    private byte[] content;

    boolean isPrivate;
}
