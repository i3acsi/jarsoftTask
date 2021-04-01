package ru.gasevsky.jarsoft.config;

import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Bean
    public WebServerFactoryCustomizer<ConfigurableServletWebServerFactory> webServerCustomizer() {
        return container -> {
            container.addErrorPages(new ErrorPage(HttpStatus.NOT_FOUND, "/"));
        };
    }



//    @Bean
//    public StringHttpMessageConverter messageConverter(){
//        StringHttpMessageConverter messageConverter = new StringHttpMessageConverter();
//        messageConverter.setSupportedMediaTypes(List.of(new MediaType("application", "json", StandardCharsets.UTF_8),
//                new MediaType("text", "plain", StandardCharsets.UTF_8)));
//        return messageConverter;
//    }
}
