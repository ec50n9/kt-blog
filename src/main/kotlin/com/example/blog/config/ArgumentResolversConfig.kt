package com.example.blog.config

import com.example.blog.annotation.CurrentUserArgumentResolver
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class ArgumentResolversConfig : WebMvcConfigurer {
    override fun addArgumentResolvers(resolvers: MutableList<HandlerMethodArgumentResolver>) {
        resolvers.add(currentUserArgumentResolver())
        super.addArgumentResolvers(resolvers)
    }

    @Bean
    fun currentUserArgumentResolver() = CurrentUserArgumentResolver()
}