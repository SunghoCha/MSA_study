package com.sh.apigatewayservice.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter @Setter
@Component
@ConfigurationProperties(prefix = "token")
public class TokenProperties {

    private long expirationTime;
    private String secret;


}
