package com.example.blog.service

import com.example.blog.BlogProperties
import com.example.blog.annotation.LoginRequired
import com.example.blog.domain.User
import com.example.blog.repo.UserRepository
import com.example.blog.utils.JWTUtils
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes
import org.springframework.web.server.ResponseStatusException

/**
 * 用户登录业务
 */
@Service
class AuthService(
    private val userRepository: UserRepository,
    private val blogProperties: BlogProperties
) {
    companion object {
        private val currentUser = ThreadLocal<User?>()
    }

    private val logger = LoggerFactory.getLogger(AuthService::class.java)

    /**
     * 从请求头中获取登录数据，并且获取到当前登录的用户，存在线程变量中
     * 只在 @LoginRequired 的处理逻辑中调用
     */
    fun initCurrentUserFromRequest() {
        // 获取请求信息
        val request = (RequestContextHolder.getRequestAttributes() as ServletRequestAttributes).request
        logger.info("=====进入登录检验=====")

        // 获取 token
        val token = request.getHeader(blogProperties.jwt.tokenName)
        if (token.isNullOrBlank())
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "请登录")

        // 校验 token
        val username = JWTUtils.decodeTokenAndGetUsername(token)
            ?: throw ResponseStatusException(HttpStatus.BAD_REQUEST, "登录信息无效")

        // 获取用户
        val user = userRepository.findByUsername(username)
            ?: throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "用户不存在")

        // 单点登录
        if (user.token != token) {
            throw if (user.token == null)
                ResponseStatusException(HttpStatus.UNAUTHORIZED, "用户未登录，请重新登录")
            else
                ResponseStatusException(HttpStatus.UNAUTHORIZED, "登录信息过期，请重新登录")
        }
        currentUser.set(user)
    }

    /**
     * 检查 currentUser 是否未初始化
     */
    fun currentUserNotInit() = currentUser.get() == null

    /**
     * 获取当前登录用户，前提是已经登录
     */
    @LoginRequired
    fun getCurrentUser(): User = currentUser.get()!!
}