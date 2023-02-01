package com.example.blog

import com.example.blog.config.BlogProperties
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@SpringBootApplication
@EnableConfigurationProperties(BlogProperties::class)
@EnableJpaAuditing
class BlogApplication

fun main(args: Array<String>) {
    runApplication<BlogApplication>(*args)
}
