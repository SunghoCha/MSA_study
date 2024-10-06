package com.sh.apigatewayservice.filter;

import com.sh.apigatewayservice.config.TokenProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.apache.el.parser.Token;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class AuthorizationHeaderFilter extends AbstractGatewayFilterFactory<AuthorizationHeaderFilter.Config> {

    private final TokenProperties tokenProperties;

    public AuthorizationHeaderFilter(TokenProperties tokenProperties) {
        super(Config.class);
        this.tokenProperties = tokenProperties;
    }

    // login -> token > users(with token) -> header(include token)
    @Override
    public GatewayFilter apply(AuthorizationHeaderFilter.Config config) {
        return (((exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            if (!request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                return onError(exchange, "No authorization header", HttpStatus.UNAUTHORIZED);
            }

            String authorizationHeader = request.getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
            String jwt = authorizationHeader.replace("Bearer", "");

            if (!isJwtValid(jwt)) {
                return onError(exchange, "JWT token is not valid", HttpStatus.UNAUTHORIZED);
            }

            return chain.filter(exchange);
        }));
    }


    private boolean isJwtValid(String jwt) {
        boolean returnValue = true;

        String subject = null;

        try {
            JwtParser jwtParser = Jwts.parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor(tokenProperties.getSecret().getBytes()))
                    .build();

            //subject = jwtParser.parseClaimsJws(jwt).getBody().getSubject();
            Jws<Claims> claimsJws = jwtParser.parseClaimsJws(jwt);
            subject = claimsJws.getBody().getSubject();

//            subject = Jwts.parserBuilder()
//                    .setSigningKey(Keys.hmacShaKeyFor(tokenProperties.getSecret().getBytes()))
//                    .build()
//                    .parseClaimsJws(jwt).getBody()
//                    .getSubject();
        } catch (Exception e) {
            returnValue = false;
        }

        if (subject == null || subject.isBlank()) {
            returnValue = false;
        }

        return returnValue;
    }

    // Mono, Flux -> Spring WebFlux
    private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus httpStatus) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);

        log.error(err);
        return response.setComplete();

    }

    public static class Config {

    }
}
