package com.example.blog.interceptor

import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class CorsConfig : WebMvcConfigurer {

    override fun addCorsMappings(registry: CorsRegistry) {
        registry.addMapping("/**")
            .allowedOrigins("*")
            .allowedMethods("GET", "POST", "PUT", "HEAD", "PATCH", "DELETE", "OPTIONS", "TRACE")
            .allowCredentials(false)
            .allowedHeaders("*")
            .maxAge(3600)
    }
}