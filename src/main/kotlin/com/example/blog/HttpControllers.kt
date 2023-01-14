package com.example.blog

import com.example.blog.utils.JWTUtils
import com.example.blog.utils.MessageDigestUtils
import org.springframework.http.HttpStatus
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("/api/articles")
class ArticleController(private val repository: ArticleRepository) {

    @GetMapping
    fun findAll() = repository.findAllByOrderByAddedAtDesc()

    @GetMapping("/{slug}")
    fun findOne(@PathVariable slug: String) =
        repository.findBySlug(slug) ?: throw ResponseStatusException(
            HttpStatus.NOT_FOUND,
            "文章不存在"
        )
}

@RestController
@RequestMapping("/api/users")
class UserController(private val repository: UserRepository) {

    @GetMapping
    fun findAll(): Iterable<User> = repository.findAll()

    @GetMapping("/{username}")
    fun findOne(@PathVariable username: String) =
        repository.findByUsername(username) ?: throw ResponseStatusException(
            HttpStatus.NOT_FOUND,
            "用户不存在"
        )
}

@RestController
@RequestMapping("/api/auth")
class AuthController(private val repository: UserRepository) {

    @LoginRequired
    @PostMapping("/login")
    fun login(@Validated @RequestBody userAuthVO: UserAuthVO): CommonResult<out String> {
        val user = repository.findByUsername(userAuthVO.username!!)
        if (user == null || user.password != MessageDigestUtils.md5(userAuthVO.password!!))
            return CommonResult.fail("用户名或密码不正确", HttpStatus.UNAUTHORIZED)
        // 生成 token
        val token = JWTUtils.generateToken(user.username)
        return CommonResult.ok(token, "登录成功")
    }
}
