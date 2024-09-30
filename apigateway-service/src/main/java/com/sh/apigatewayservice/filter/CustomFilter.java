package com.sh.apigatewayservice.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class CustomFilter extends AbstractGatewayFilterFactory<CustomFilter.Config> {


    public CustomFilter() {
        super(Config.class);
    }

    /*
        Pre 필터는 chain.filter(exchange) 호출 이전에 실행되고, Post 필터는 chain.filter(exchange) 이후에 실행
        비동기 방식으로 로그 기록과 같은 부가적인 작업을 분리하여 응답 대기 시간 단축
        비동기 로깅은 성능상의 이점이 있지만, 응답 실패와 관련된 문제를 간과할 수 있는 위험이 있음 (상태 불일치)
     */
    @Override
    public GatewayFilter apply(Config config) {
        // Custom Pre Filter
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            ServerHttpResponse response = exchange.getResponse();
            log.info("Custom PRE filter: request id -> {}", request.getId());

            // Custom Post Filter
            return chain.filter(exchange).then(Mono.fromRunnable(() -> {
                log.info("Custom POST filter: response code -> {}", response.getStatusCode());
            }));

        };
    }

    public static class Config {
        // Put the configuration properties
    }
}