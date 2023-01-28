package com.example.blog.annotation

import com.example.blog.service.AuthService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.MethodParameter
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer

@Target(AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
annotation class CurrentUser

class CurrentUserArgumentResolver : HandlerMethodArgumentResolver {

    @Autowired
    private lateinit var authService: AuthService

    val logger = LoggerFactory.getLogger(CurrentUserArgumentResolver::class.java)

    override fun supportsParameter(parameter: MethodParameter) =
        parameter.hasParameterAnnotation(CurrentUser::class.java)

    override fun resolveArgument(
        parameter: MethodParameter,
        mavContainer: ModelAndViewContainer?,
        webRequest: NativeWebRequest,
        binderFactory: WebDataBinderFactory?
    ): Any {
        return authService.getCurrentUser()
    }
}
