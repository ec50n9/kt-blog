package com.example.blog

import com.example.blog.domain.User
import com.example.blog.service.AuthService
import org.slf4j.LoggerFactory
import org.springframework.data.domain.AuditorAware
import org.springframework.stereotype.Component
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes
import java.util.*

@Component
class AuditorAwareImpl(val authService: AuthService) : AuditorAware<User> {

    private val logger = LoggerFactory.getLogger(AuditorAwareImpl::class.java)

    override fun getCurrentAuditor(): Optional<User> {
        logger.info("正在读取当前用户..")
        val requestAttributes = RequestContextHolder.getRequestAttributes()
        // 非网络请求
        if (RequestContextHolder.getRequestAttributes() == null)
            return Optional.empty()
        // 登录时写入用户token
        val uri = (requestAttributes as ServletRequestAttributes).request.requestURI
        if (uri == "/api/auth/login")
            return Optional.empty()
        return Optional.of(authService.getCurrentUser())
    }
}