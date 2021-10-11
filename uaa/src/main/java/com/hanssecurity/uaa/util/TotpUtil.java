package com.hanssecurity.uaa.util;

import com.eatthepath.otp.TimeBasedOneTimePasswordGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.stereotype.Component;

import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.time.Instant;
import java.util.Base64;
import java.util.Optional;

/**
 * @author hans
 */

@Slf4j
@RequiredArgsConstructor
@Component
public class TotpUtil {
    private static final long TIME_STEP = 60 * 5L;
    private static final int PASSWORD_Length = 6;
    private KeyGenerator keyGenerator;
    private TimeBasedOneTimePasswordGenerator totp;

    /**
     * init code module
     */
    {
        try {
            totp = new TimeBasedOneTimePasswordGenerator(Duration.ofSeconds(TIME_STEP), PASSWORD_Length);
            // init keyGenerator
            keyGenerator = keyGenerator.getInstance(totp.getAlgorithm());

            // SHA-1 and SHA-256 need 64 word 512 bit key;
            // SHA512 need 128 words 1024 bit key
            keyGenerator.init(512);

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            log.error("No algorithm {}", e.getLocalizedMessage());
        }
    }

    public  String createTotp(Key key, Instant time) throws InvalidKeyException {
        val password = totp.generateOneTimePassword(key, time);
        val format = "%0" + PASSWORD_Length + "d";
        // it might give 7, 77, 777...

        return String.format(format, password);
    }

    public Optional<String> createTotp(String strKey) {

        try {
            return Optional.of(createTotp(decodeKeyFromString(strKey), Instant.now()));
        } catch (InvalidKeyException e){
            return Optional.empty();
        }
    }

    /**
     * Valid TOTP
     *
     * @param key
     * @param code
     * @return whether is same
     * @throws InvalidKeyException
     */
    public boolean verifyTotp(Key key, String code) throws InvalidKeyException {
        val now = Instant.now();

        return code.equals(createTotp(key, now));
    }

    private Key generateKey() {
        return keyGenerator.generateKey();
    }

    private String encodeKeyToString(Key key) {
        return Base64.getEncoder().encodeToString(key.getEncoded());
    }

    public String encodeKeyToString() {
        return encodeKeyToString(generateKey());
    }

    public Key decodeKeyFromString(String strKey) {
        return new SecretKeySpec(Base64.getDecoder().decode(strKey), totp.getAlgorithm());
    }

    public long getTimeStepInSeconds() {
        return TIME_STEP;
    }

}
