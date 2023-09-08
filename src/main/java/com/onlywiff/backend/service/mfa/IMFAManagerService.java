package com.onlywiff.backend.service.mfa;

import dev.samstevens.totp.exceptions.QrGenerationException;
import reactor.core.publisher.Mono;

public interface IMFAManagerService {

    String generateSecret();
    Mono<String> getQRCode(final String secret, final String email) throws QrGenerationException;
    Mono<Boolean> verifyCode(final String secret, final String code);
    Mono<String[]> getRecoveryCodes();

}
