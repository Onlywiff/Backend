package com.onlywiff.backend.service;

import com.onlywiff.backend.api.GenericObjectResponse;
import com.onlywiff.backend.repository.upload.Upload;
import com.onlywiff.backend.repository.upload.UploadRepository;
import com.onlywiff.backend.repository.user.User;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

import java.io.IOException;

@Service("uploadService")
public class UploadService {

    SessionService sessionService;
    UploadRepository uploadRepository;

    @Autowired
    public UploadService(SessionService sessionService,
                         UploadRepository uploadRepository) {
        this.sessionService = sessionService;
        this.uploadRepository = uploadRepository;
    }

    public Mono<GenericObjectResponse<Upload>> save(String sessionToken, MultipartFile file) {
        return sessionService.checkSession(sessionToken).flatMap(tuple -> {
            if (tuple.getT1()) {
                User user = tuple.getT2().getUser();
                byte[] bytes;
                try {
                    bytes = IOUtils.toByteArray(file.getInputStream());
                } catch (IOException e) {
                    return Mono.error(new RuntimeException(e));
                }
                return uploadRepository.save(new Upload(file.getName(), user, bytes)).flatMap(upload -> Mono.just(new GenericObjectResponse<Upload>(true, upload, "Success")));
            } else {
                return Mono.just(new GenericObjectResponse<Upload>(false, null,"Needs MFA Login"));
            }
        }).onErrorReturn(new GenericObjectResponse<Upload>(false, null, "Upload failed"));
    }

}
