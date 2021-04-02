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
        return container -> container.addErrorPages(new ErrorPage(HttpStatus.NOT_FOUND, "/"));
    }

//    @Bean
//    public FilterRegistrationBean<RequestFilter> requestFilter() {
//        FilterRegistrationBean<RequestFilter> registrationBean = new FilterRegistrationBean<>();
//        registrationBean.setFilter(new RequestFilter());
//        registrationBean.addUrlPatterns("/bid");
//        return registrationBean;
//    }

}
