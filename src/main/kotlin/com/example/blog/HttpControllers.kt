package com.example.blog

import com.example.blog.utils.JWTUtils
import com.example.blog.utils.MessageDigestUtils
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import javax.servlet.http.HttpServletRequest

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

    private val log = LoggerFactory.getLogger("AuthApi")

    @PostMapping("/login")
    fun login(@Validated @RequestBody userAuthVO: UserAuthVO): CommonResult<out String> {
        val user = repository.findByUsername(userAuthVO.username!!)
        if (user == null || user.password != MessageDigestUtils.md5(userAuthVO.password!!))
            return CommonResult.fail("用户名或密码不正确", HttpStatus.UNAUTHORIZED)

        // 生成 token
        val token = JWTUtils.generateToken(user.username)

        // 存储token到用户信息中
        user.token = token
        repository.save(user)

        return CommonResult.ok(token, "登录成功")
    }

    @LoginRequired
    @PostMapping("/logout")
    fun logout(request: HttpServletRequest) {
        val user: User = request.getAttribute("user") as User
        log.info(user.username)
    }

    @LoginRequired
    @GetMapping("/test")
    fun test() = CommonResult.ok(null, "你好👋")
}
