package com.onlywiff.backend.service.mfa;

import dev.samstevens.totp.code.CodeVerifier;
import dev.samstevens.totp.code.HashingAlgorithm;
import dev.samstevens.totp.exceptions.QrGenerationException;
import dev.samstevens.totp.qr.QrData;
import dev.samstevens.totp.qr.QrDataFactory;
import dev.samstevens.totp.qr.QrGenerator;
import dev.samstevens.totp.recovery.RecoveryCodeGenerator;
import dev.samstevens.totp.secret.SecretGenerator;
import dev.samstevens.totp.util.Utils;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service("mfaManagerService")
public class MFAManagerService implements IMFAManagerService {

    @Autowired
    private SecretGenerator secretGenerator;

    @Resource
    private QrDataFactory qrDataFactory;

    @Resource
    private QrGenerator qrGenerator;

    @Resource
    private CodeVerifier codeVerifier;

    @Resource
    private RecoveryCodeGenerator recoveryCodeGenerator;

    @Override
    public String generateSecret() {
        return secretGenerator.generate();
    }

    @Override
    public Mono<String> getQRCode(String secret, String email) throws QrGenerationException {
        QrData data = qrDataFactory.newBuilder()
                .label("OnlyWiff - " + email)
                .secret(secret)
                .issuer("OnlyWiff")
                .algorithm(HashingAlgorithm.SHA256)
                .digits(6)
                .period(30)
                .build();
        return Mono.just(Utils.getDataUriForImage(
                qrGenerator.generate(data),
                qrGenerator.getImageMimeType()
        )).onErrorReturn("");
    }

    @Override
    public Mono<Boolean> verifyCode(String code, String secret) {
        return Mono.just(codeVerifier.isValidCode(secret, code)).onErrorReturn(false);
    }

    @Override
    public Mono<String[]> getRecoveryCodes() {
        return Mono.just(recoveryCodeGenerator.generateCodes(10));
    }
}
