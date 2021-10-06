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
    private SmsProvider smsProvider = new SmsProvider();

    @Getter
    @Setter
    private Ali ali = new Ali();

    @Getter
    @Setter
    private LeanCloud leanCloud = new LeanCloud();

    @Getter
    @Setter
    private EmailProvider emailProvider = new EmailProvider();

    @Getter
    @Setter
    public static class Ali {
        private String apiKey;
        private String apiSecret;
    }

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

    @Getter
    @Setter
    public static class SmsProvider {
        private String name;
        private String apiUrl;
    }

    @Getter
    @Setter
    public static class LeanCloud {
        private String appId;
        private String appKey;
    }

    @Getter
    @Setter
    public static class EmailProvider {
        private String name;
        private String apiKey;
    }
}
