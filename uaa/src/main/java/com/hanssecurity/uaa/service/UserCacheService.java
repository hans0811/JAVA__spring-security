package com.hanssecurity.uaa.service;

import com.hanssecurity.uaa.config.Constants;
import com.hanssecurity.uaa.domain.User;
import com.hanssecurity.uaa.util.CryptoUtil;
import com.hanssecurity.uaa.util.TotpUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.redisson.api.RMapCache;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import java.security.InvalidKeyException;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * @author hans
 */

@Slf4j
@RequiredArgsConstructor
@Service
public class UserCacheService {

    private final RedissonClient redisson;
    private final TotpUtil totpUtil;
    private final CryptoUtil cryptoUtil;

    public String cacheUser(User user) {
        val mfaId = cryptoUtil.randomAlphanumeric(12);
        RMapCache<String, User> cache = redisson.getMapCache(Constants.CACHE_MFA);
        if(!cache.containsKey(mfaId)) {
            cache.put(mfaId, user, totpUtil.getTimeStepInSeconds(), TimeUnit.SECONDS);
        }
        return mfaId;
    }

    public Optional<User> retrieveUser(String mfaId){

        RMapCache<String, User> cache = redisson.getMapCache(Constants.CACHE_MFA);
        if(cache.containsKey(mfaId)) {
            return Optional.of(cache.get(mfaId));
        }
        return Optional.empty();
    }

    public Optional<User> verifyTotp(String mfaId, String code) {
        RMapCache<String, User> cache = redisson.getMapCache(Constants.CACHE_MFA);

        if(!cache.containsKey(mfaId) || cache.get(mfaId) == null){
            return Optional.empty();
        }

        val cachedUser = cache.get(mfaId);

        try {
            val isValid = totpUtil.verifyTotp(totpUtil.decodeKeyFromString(cachedUser.getMfaKey()),code);

            if(!isValid) {
                return Optional.empty();
            }
            cache.remove(mfaId);
            return Optional.of(cachedUser);

        }catch (InvalidKeyException e ){
            e.printStackTrace();
        }

        return Optional.empty();
    }

}
