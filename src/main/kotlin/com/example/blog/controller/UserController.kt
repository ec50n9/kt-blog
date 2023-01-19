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

    private val userMapper = UserMapper.INSTANCE

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
        val entity = userMapper.toEntity(modifyDto)

        if (repository.existsByUsername(entity.username))
            throw ResponseStatusException(HttpStatus.ACCEPTED, "用户名已存在")

        val user = repository.save(entity)
        return CommonResponse.ok(userMapper.toDto(user), "创建成功", HttpStatus.CREATED)
    }

    @PutMapping("/{id}")
    fun update(@Validated @RequestBody modifyDto: UserModifyDto, @PathVariable id: Long): CommonResponse<UserViewDto> {
        val entity = userMapper.toEntity(modifyDto)
        entity.id = id
        val user = repository.save(entity)
        return CommonResponse.ok(userMapper.toDto(user), "更新成功")
    }

    @PatchMapping("/{id}")
    fun patch(@RequestBody modifyDto: UserModifyDto, @PathVariable id: Long): CommonResponse<UserViewDto> {
        val userOptional = repository.findById(id)
        if (userOptional.isEmpty) throw ResponseStatusException(HttpStatus.NOT_FOUND, "用户不存在")
        var user = userOptional.get()
        userMapper.partialUpdate(modifyDto, user)
        user = repository.save(user)
        return CommonResponse.ok(userMapper.toDto(user), "更新成功")
    }
}
