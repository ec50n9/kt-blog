package com.example.blog.controller

import com.example.blog.annotation.LoginRequired
import com.example.blog.domain.CommonResponse
import com.example.blog.domain.dto.UserModifyDto
import com.example.blog.domain.dto.UserViewDto
import com.example.blog.domain.mapper.UserMapper
import com.example.blog.repo.UserRepository
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("/api/users")
class UserController(
    private val userRepository: UserRepository,
    private val userMapper: UserMapper
) {

    @GetMapping
    fun findAll(
        @RequestParam(defaultValue = "1") page: Int,
        @RequestParam(defaultValue = "10") size: Int
    ): Iterable<UserViewDto> {
        val pageRequest = PageRequest.of(page - 1, size, Sort.by("username"))
        val userList = userRepository.findAll(pageRequest)
        return userMapper.toDto(userList)
    }

    @GetMapping("/{username}")
    fun findOne(@PathVariable username: String): UserViewDto {
        val user = userRepository.findByUsername(username)
            ?: throw ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "用户不存在"
            )
        return userMapper.toDto(user)
    }

    @LoginRequired
    @PostMapping
    fun create(@Validated @RequestBody modifyDto: UserModifyDto): CommonResponse<UserViewDto> {
        val entity = userMapper.toEntity(modifyDto)

        if (userRepository.existsByUsername(entity.username))
            throw ResponseStatusException(HttpStatus.ACCEPTED, "用户名已存在")

        val user = userRepository.save(entity)
        return CommonResponse.ok(userMapper.toDto(user), "创建成功", HttpStatus.CREATED)
    }

    @LoginRequired
    @PutMapping("/{id}")
    fun update(
        @Validated @RequestBody modifyDto: UserModifyDto,
        @PathVariable id: String
    ): CommonResponse<UserViewDto> {
        val entity = userMapper.toEntity(modifyDto)
        entity.id = id
        val user = userRepository.save(entity)
        return CommonResponse.ok(userMapper.toDto(user), "更新成功")
    }

    @LoginRequired
    @PatchMapping("/{id}")
    fun patch(@RequestBody modifyDto: UserModifyDto, @PathVariable id: String): CommonResponse<UserViewDto> {
        var user =
            userRepository.findByIdOrNull(id) ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "用户不存在")
        userMapper.partialUpdate(modifyDto, user)
        user = userRepository.save(user)
        return CommonResponse.ok(userMapper.toDto(user), "更新成功")
    }

    @LoginRequired
    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: String): CommonResponse<Nothing> {
        val user =
            userRepository.findByIdOrNull(id) ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "用户不存在")
        userRepository.delete(user)
        return CommonResponse.ok(null, "删除成功")
    }
}
