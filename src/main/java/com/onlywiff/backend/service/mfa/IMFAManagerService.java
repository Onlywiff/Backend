package com.onlywiff.backend.service.mfa;

import dev.samstevens.totp.exceptions.QrGenerationException;

public interface IMFAManagerService {

    String generateSecret();
    String getQRCode(final String secret, final String email) throws QrGenerationException;
    boolean verifyCode(final String secret, final String code);

}
