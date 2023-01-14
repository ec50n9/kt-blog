package com.example.blog

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
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