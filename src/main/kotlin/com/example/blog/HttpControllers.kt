package com.example.blog

import org.springframework.http.HttpStatus
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

    @PostMapping("/login")
    fun login(@RequestParam username: String, @RequestParam password: String): CommonResult<out UserVO> {
        val user = repository.findByUsername(username)
        if (user == null || user.password != MessageDigestUtils.md5(password))
            return CommonResult.fail("用户名或密码不正确", HttpStatus.UNAUTHORIZED)
        return CommonResult.ok(UserVO(user))
    }
}
