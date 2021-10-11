package com.hanssecurity.uaa.config;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.profile.DefaultProfile;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author hans
 */
@RequiredArgsConstructor
//@Configuration
public class AliConfig {

    private final AppProperties appProperties;

    @Bean
    public IAcsClient iAcsClient() {
        DefaultProfile profile = DefaultProfile.getProfile(
                "HongKong",
                appProperties.getAli().getApiKey(),
                appProperties.getAli().getApiSecret()
        );

        return new DefaultAcsClient(profile);
    }
}
