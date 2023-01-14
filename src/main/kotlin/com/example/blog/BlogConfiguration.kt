package com.example.blog

import com.example.blog.utils.MessageDigestUtils
import org.springframework.boot.ApplicationRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.security.MessageDigest

@Configuration
class BlogConfiguration {

    @Bean
    fun databaseInitializer(
        userRepository: UserRepository,
        articleRepository: ArticleRepository
    ) = ApplicationRunner {
        MessageDigest.getInstance("MD5")
        val ec50n9 = userRepository.save(
            User(
                username = "ec50n9",
                password = MessageDigestUtils.md5("1234"),
                firstname = "Gaosong",
                lastname = "Liang"
            )
        )
        articleRepository.save(
            Article(
                title = "Reactor Bismuth is out",
                headline = "Lorem ipsum",
                content = "dolor sit amet",
                author = ec50n9
            )
        )
        articleRepository.save(
            Article(
                title = "Reactor Aluminium has landed",
                headline = "Lorem ipsum",
                content = "dolor sit amet",
                author = ec50n9
            )
        )
    }
}