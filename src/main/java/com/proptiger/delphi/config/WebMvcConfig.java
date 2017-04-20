package com.proptiger.delphi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

@EnableWebMvc
@EnableAsync
@Configuration
@PropertySource({ "classpath:application.properties", "classpath:config.properties" })
@EnableMongoRepositories(basePackages = "com.proptiger.delphi")
public class WebMvcConfig extends WebMvcConfigurerAdapter {

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertyPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/apidocs").setViewName("ui");
    }

    @Bean
    public InternalResourceViewResolver viewResolver() {
        InternalResourceViewResolver resolver = new InternalResourceViewResolver();
        resolver.setPrefix("/WEB-INF/swagger/");
        resolver.setSuffix(".jsp");
        return resolver;
    }

    @Override
    public void addResourceHandlers(final ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/lib/**").addResourceLocations("/WEB-INF/swagger/lib/");
        registry.addResourceHandler("/css/**").addResourceLocations("/WEB-INF/swagger/css/");
        registry.addResourceHandler("/fonts/**").addResourceLocations("/WEB-INF/swagger/fonts/");
        registry.addResourceHandler("/images/**").addResourceLocations("/WEB-INF/swagger/images/");
        registry.addResourceHandler("/**").addResourceLocations("/WEB-INF/swagger/");
    }
}