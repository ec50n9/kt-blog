package com.example.blog

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties("blog")
data class BlogProperties(var title: String, val banner: Banner, val jwt: JWT = JWT()) {
    data class Banner(val title: String? = null, val content: String)
    data class JWT(val tokenName: String = "token")
}