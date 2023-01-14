package com.example.blog.interceptor

import com.example.blog.LoginRequired
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.web.method.HandlerMethod
import org.springframework.web.servlet.HandlerInterceptor
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class AuthenticationInterceptor : HandlerInterceptor {

    private val log: Logger = LoggerFactory.getLogger("Auth")
    private val TOKEN_NAME = "token"

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        log.info("==========进入拦截器==========")
        // 如果不是映射到方法直接跳过，访问资源
        if (handler !is HandlerMethod)
            return true;
        // 为空就返回错误
        val token = request.getHeader(TOKEN_NAME)
        if (null == token || "" == token.trim())
            return false;
        log.info("==========token: $token")

        // 获取方法上的注解
        val handlerMethod: HandlerMethod = handler
        var requirePermission = handlerMethod.method.getAnnotation(LoginRequired::class.java)
        // 如果方法上没有，则获取类上的注解
        if (requirePermission == null)
            requirePermission = handlerMethod.method.declaringClass.getAnnotation(LoginRequired::class.java)

        // 权限检查逻辑
        if (requirePermission != null && requirePermission.required) {
            log.info("需要登录")
        }
        return true
    }
}