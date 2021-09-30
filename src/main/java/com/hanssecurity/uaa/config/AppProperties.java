package com.hanssecurity.uaa.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author hans
 * hans.jwt.access-Token-Expire-Time =
 */
@Configuration
@ConfigurationProperties(prefix= "hans" )
public class AppProperties {

    @Getter
    @Setter
    private Jwt jwt = new Jwt();

    @Getter
    @Setter
    public static class Jwt {

        // custom setting header and prefix
        private String header = "Authorization";
        private String prefix = "Beaeer ";

        // Access Token
        private Long accessTokenExpireTime = 60_000L;
        // Access Refresh Token
        private Long refreshTokenExpireTime = 30 * 24 * 3600 * 1000L;
    }
}
