package com.example.blog.controller

import com.example.blog.UserRepository
import com.example.blog.annotation.NotResponseAdvice
import com.example.blog.domain.CommonResponse
import com.example.blog.domain.dto.UserModifyDto
import com.example.blog.domain.dto.UserViewDto
import com.example.blog.domain.mapper.UserMapper
import org.springframework.http.HttpStatus
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("/api/users")
class UserController(private val repository: UserRepository) {

    @NotResponseAdvice
    @GetMapping
    fun findAll() = UserMapper.INSTANCE.toDto(repository.findAll())

    @GetMapping("/{username}")
    fun findOne(@PathVariable username: String): UserViewDto {
        val user = repository.findByUsername(username)
            ?: throw ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "用户不存在"
            )
        return UserMapper.INSTANCE.toDto(user)
    }

    @PostMapping
    fun create(@Validated @RequestBody modifyDto: UserModifyDto): CommonResponse<UserViewDto> {
        val userConverter = UserMapper.INSTANCE
        val userModel = userConverter.toEntity(modifyDto)

        if (repository.existsByUsername(userModel.username))
            throw ResponseStatusException(HttpStatus.ACCEPTED, "用户名已存在")

        val user = repository.save(userModel)
        return CommonResponse.ok(userConverter.toDto(user), "创建成功", HttpStatus.CREATED)
    }
}
