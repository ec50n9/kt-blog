package com.example.blog.annotation

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
annotation class RoleCheck(val value: Array<String>, val requireAll: Boolean = true)
