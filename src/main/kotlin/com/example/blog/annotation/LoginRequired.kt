package com.example.blog.annotation

@Target(AnnotationTarget.FUNCTION, AnnotationTarget.TYPE, AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class LoginRequired(
    val required: Boolean = true,
)
