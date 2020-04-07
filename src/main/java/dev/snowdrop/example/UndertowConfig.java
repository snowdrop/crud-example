package dev.snowdrop.example;

import io.undertow.UndertowOptions;
import io.undertow.security.api.AuthenticationMechanismContext;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.servlet.api.FilterInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.embedded.undertow.UndertowBuilderCustomizer;
import org.springframework.boot.web.embedded.undertow.UndertowServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collection;
import java.util.Map;

@Configuration
public class UndertowConfig {
    
    @Value("9090")
    int ajpPort;

    @Bean
    public UndertowServletWebServerFactory servletContainer() {
        UndertowServletWebServerFactory undertow = new UndertowServletWebServerFactory();
//        HttpHandler rootHandler = new HttpHandler() {
//            @Override
//            public void handleRequest(HttpServerExchange exchange) throws Exception {
//                exchange.getSecurityContext().setAuthenticationRequired();
//            }
//        };
//        undertow.addBuilderCustomizers(builder -> builder.addAjpListener(9090,"localhost", rootHandler));
        undertow.addBuilderCustomizers(builder -> builder.addAjpListener(9090,"0.0.0.0"));
        return undertow;
    }
}
