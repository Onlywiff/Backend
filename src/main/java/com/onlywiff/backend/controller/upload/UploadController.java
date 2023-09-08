package com.onlywiff.backend.controller.upload;

import com.onlywiff.backend.api.GenericObjectResponse;
import com.onlywiff.backend.repository.upload.Upload;
import com.onlywiff.backend.service.UploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(value = "/api/upload", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
public class UploadController {

    UploadService uploadService;

    @Autowired
    public UploadController(UploadService uploadService) {
        this.uploadService = uploadService;
    }

    @RequestMapping(value = "/file")
    public Mono<GenericObjectResponse<Upload>> uploadFile(@RequestHeader(name = "Authorization") String sessionToken, @RequestParam("file") MultipartFile file) {
        return uploadService.save(sessionToken, file);
    }
}
