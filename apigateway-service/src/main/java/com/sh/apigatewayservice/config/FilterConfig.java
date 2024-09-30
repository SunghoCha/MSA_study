package com.sh.apigatewayservice.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//@Configuration
public class FilterConfig {

    /*
       일반적인 API Gateway 설정에서는 application.yml 방식을 사용하는 것이 더 권장
       동적인 라우팅을 설정하거나, 프로그래밍적으로 복잡한 조건을 추가해야 할 때는 이런 Java 코드 방식이 더 나을수 있음
     */

    //@Bean
    public RouteLocator gatewayRoutes(RouteLocatorBuilder builder) {

        return builder.routes()
                .route(routeBuilder -> routeBuilder
                        .path("/first-service/**")
                        .filters(filterSpec -> filterSpec
                                .addRequestHeader("first-service-request", "first-service-request")
                                .addResponseHeader("first-service-response", "first-service-response")
                        )
                        .uri("http://localhost:8081"))
                .route(routeBuilder -> routeBuilder
                        .path("/second-service/**")
                        .filters(filterSpec -> filterSpec
                                .addRequestHeader("second-service-request", "second-service-request")
                                .addResponseHeader("second-service-response", "second-service-response")
                        )
                        .uri("http://localhost:8082"))
                .build();
    }
}
