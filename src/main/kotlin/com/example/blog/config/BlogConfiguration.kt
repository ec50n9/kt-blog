package com.example.blog.config

import com.example.blog.domain.Article
import com.example.blog.domain.User
import com.example.blog.repo.ArticleRepository
import com.example.blog.repo.UserRepository
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
                title = "Vue 的 diff 算法是什么？",
                headline = "大家或多或少都听说过 diff 算法，那么它到底是啥呢？",
                content = """# 今天天气不错哦
                    |## 这是二级标题
                    |* aaaaa
                    |* bbbbb
                    |* ccccc
                    |> 你觉得今天怎么样呢
                    |```shell
                    |echo 'hello, wrold'
                    |```
                """.trimMargin(),
                author = ec50n9
            )
        )
    }
}