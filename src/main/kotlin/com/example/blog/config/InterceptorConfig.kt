package com.example.blog.config

import com.example.blog.interceptor.AuthenticationInterceptor
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class InterceptorConfig : WebMvcConfigurer {

    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(authenticationInterceptor())
            .addPathPatterns("/**")
    }

    @Bean
    fun authenticationInterceptor() = AuthenticationInterceptor()
}