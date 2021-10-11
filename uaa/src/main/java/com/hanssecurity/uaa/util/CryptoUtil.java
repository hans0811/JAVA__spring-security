package com.hanssecurity.uaa.util;

import lombok.val;
import org.springframework.stereotype.Component;

import java.util.Random;

/**
 * @author hans
 */
@Component
public class CryptoUtil {

    public String randomAlphanumeric(int targetStringLength) {
        int leftLimit = 40;
        int rightLimit = 122;
        val random = new Random();
        return random.ints(leftLimit, rightLimit+1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }
}
