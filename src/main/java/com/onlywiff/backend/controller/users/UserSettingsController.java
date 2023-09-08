package com.onlywiff.backend.controller.users;

import com.onlywiff.backend.service.UserManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/user/settings", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
public class UserSettingsController {

    UserManagerService userManagerService;

    @Autowired
    public UserSettingsController(UserManagerService userManagerService) {
        this.userManagerService = userManagerService;
    }
}
