package com.example.blog.controller

import com.example.blog.*
import com.example.blog.annotation.LoginRequired
import com.example.blog.domain.CommonResponse
import com.example.blog.domain.User
import com.example.blog.domain.dto.UserAuthDto
import com.example.blog.repo.UserRepository
import com.example.blog.utils.JWTUtils
import com.example.blog.utils.MessageDigestUtils
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletRequest

@RestController
@RequestMapping("/api/auth")
class AuthController(private val repository: UserRepository) {

    private val log = LoggerFactory.getLogger("AuthApi")

    @PostMapping("/login")
    fun login(@Validated @RequestBody authDto: UserAuthDto): CommonResponse<out String> {
        val user = repository.findByUsername(authDto.username!!)
        if (user == null || user.password != MessageDigestUtils.md5(authDto.password!!))
            return CommonResponse.fail("用户名或密码不正确", HttpStatus.UNAUTHORIZED)

        // 生成 token
        val token = JWTUtils.generateToken(user.id!!)

        // 存储token到用户信息中
        user.token = token
        repository.save(user)

        return CommonResponse.ok(token, "登录成功")
    }

    @LoginRequired
    @PostMapping("/logout")
    fun logout(request: HttpServletRequest) {
        val user = request.getAttribute("user") as User
        user.token = null
        repository.save(user)
    }

    @LoginRequired
    @GetMapping("/test")
    fun test() = CommonResponse.ok(null, "你好👋")
}