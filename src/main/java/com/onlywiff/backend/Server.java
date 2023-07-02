package com.onlywiff.backend;

import lombok.Getter;

@Getter
public class Server {

    private final Server instance;

    public Server() {
        instance = new Server();
    }
}
