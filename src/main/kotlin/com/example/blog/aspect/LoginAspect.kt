package com.example.blog.aspect

import com.example.blog.annotation.LoginRequired
import com.example.blog.service.AuthService
import org.aspectj.lang.JoinPoint
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Before
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Aspect
@Component
class LoginAspect(private val authService: AuthService) {

    private val logger = LoggerFactory.getLogger(LoginAspect::class.java)

    @Before("@within(loginRequired) || @annotation(loginRequired)")
    fun checkLogin(joinPoint: JoinPoint, loginRequired: LoginRequired) {
        if (loginRequired.required && authService.currentUserNotInit())
            authService.initCurrentUserFromRequest()
    }
}