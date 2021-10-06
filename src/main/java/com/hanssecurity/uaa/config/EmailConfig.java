package com.hanssecurity.uaa.config;

import com.sendgrid.SendGrid;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author hans
 */

@RequiredArgsConstructor
@Configuration
public class EmailConfig {

    private final AppProperties appProperties;

    @ConditionalOnProperty(prefix="hans.email-proivder", name = "api-key")
    @Bean
    public SendGrid sendGrid() {
        return new SendGrid(appProperties.getEmailProvider().getApiKey());
    }
}
