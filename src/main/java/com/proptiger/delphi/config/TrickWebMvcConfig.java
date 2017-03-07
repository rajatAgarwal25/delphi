package com.proptiger.delphi.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@EnableWebMvc
@EnableAsync
@Configuration
public class TrickWebMvcConfig extends WebMvcConfigurerAdapter {

    // @Bean(name = "multipartResolver")
    // public CommonsMultipartResolver getMultiPartResolver() {
    // CommonsMultipartResolver multipartResolver = new
    // CommonsMultipartResolver();
    // multipartResolver.setMaxUploadSize(104857600);
    // return multipartResolver;
    // }
}