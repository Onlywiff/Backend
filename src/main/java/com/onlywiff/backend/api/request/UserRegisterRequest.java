package com.onlywiff.backend.api.request;

public record UserRegisterRequest(String name, String displayName, String email, String password) {
}
