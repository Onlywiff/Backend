package com.onlywiff.backend.api.request;

public record UserMFARequest(boolean success, String image, String code, String message) {
}
