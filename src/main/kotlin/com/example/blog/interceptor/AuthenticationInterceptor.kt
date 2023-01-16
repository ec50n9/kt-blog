package com.example.blog.interceptor

import com.example.blog.CommonResult
import com.example.blog.LoginRequired
import com.example.blog.UserRepository
import com.example.blog.utils.JWTUtils
import com.fasterxml.jackson.databind.ObjectMapper
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.method.HandlerMethod
import org.springframework.web.servlet.HandlerInterceptor
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class AuthenticationInterceptor : HandlerInterceptor {

    @Autowired
    private lateinit var userRepository: UserRepository

    private val log: Logger = LoggerFactory.getLogger("Auth")
    private val TOKEN_NAME = "token"

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        log.info("==========进入拦截器==========")
        // 如果不是映射到方法直接跳过，访问资源
        if (handler !is HandlerMethod || !checkLoginRequired(handler))
            return true

        // 获取 token
        val token = request.getHeader(TOKEN_NAME)
        if (null == token || "" == token.trim()) {
            writeResponse(response, CommonResult.fail("缺少 $TOKEN_NAME 字段"))
            return false
        }
        log.info("==========token: $token")

        // 校验 token
        val username = JWTUtils.decodeTokenAndGetUsername(token)
        if (username == null) {
            log.error("==========用户名为空")
            writeResponse(response, CommonResult.fail("传入的 $TOKEN_NAME 无效"))
            return false
        }

        val user = userRepository.findByUsername(username)
        if (user == null) {
            log.error("==========用户 $username 不存在")
            writeResponse(response, CommonResult.fail("传入的 $TOKEN_NAME 无效"))
            return false
        }

        if (user.token != token) {
            if (user.token == null) {
                log.error("==========用户未登录")
            } else {
                log.error("==========用户在其它地方登录")
            }
            writeResponse(response, CommonResult.fail("传入的 $TOKEN_NAME 无效"))
            return false
        }

        request.setAttribute("user", user)
        return true
    }

    private fun checkLoginRequired(handler: HandlerMethod): Boolean {
        // 获取方法上的注解
        val handlerMethod: HandlerMethod = handler
        var requirePermission = handlerMethod.method.getAnnotation(LoginRequired::class.java)
        // 如果方法上没有，则获取类上的注解
        if (requirePermission == null)
            requirePermission = handlerMethod.method.declaringClass.getAnnotation(LoginRequired::class.java)
        // 权限检查逻辑
        return requirePermission != null && requirePermission.required
    }

    private fun <T> writeResponse(response: HttpServletResponse, result: CommonResult<T>) {
        response.characterEncoding = "UTF-8"
        response.contentType = "application/json; charset=utf-8"
        val writer = response.writer
        val objMapper = ObjectMapper()
        writer.print(objMapper.writeValueAsString(result))
    }
}