package com.example.blog.controller

import com.example.blog.annotation.LoginRequired
import com.example.blog.domain.CommonResponse
import com.example.blog.domain.User
import com.example.blog.domain.dto.UserModifyDto
import com.example.blog.domain.dto.UserViewDto
import com.example.blog.domain.mapper.UserMapper
import com.example.blog.repo.UserRepository
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
) : BaseController() {

    @GetMapping
    fun findAll(
        @RequestParam(defaultValue = "1") page: Int,
        @RequestParam(defaultValue = "10") size: Int,
        @RequestParam(defaultValue = "username") sortBy: String,
        @RequestParam(defaultValue = "desc") orderBy: String,
        @RequestParam(defaultValue = "") username: String
    ): Iterable<UserViewDto> {
        val pageRequest = createPageRequest<User>(page, size, orderBy, sortBy)
        val userList =
            userRepository.findByUsernameLikeIgnoreCase(
                "%$username%",
                pageRequest
            )
        return userMapper.toDto(userList)
    }

    @GetMapping("/{username}")
    fun findOne(@PathVariable username: String): UserViewDto {
        val user = userRepository.findByUsername(username)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "用户不存在")
        return userMapper.toDto(user)
    }

    @PostMapping
    fun create(@Validated @RequestBody modifyDto: UserModifyDto): CommonResponse<UserViewDto> {
        val entity = userMapper.toEntity(modifyDto)

        checkUserValid(entity)

        val user = userRepository.save(entity)
        return CommonResponse.ok(userMapper.toDto(user), "创建成功", HttpStatus.CREATED)
    }

    @PutMapping("/{id}")
    fun update(
        @Validated @RequestBody modifyDto: UserModifyDto,
        @PathVariable id: String
    ) = updateUser(id, modifyDto)

    @LoginRequired
    @PatchMapping("/{id}")
    fun patch(
        @RequestBody modifyDto: UserModifyDto,
        @PathVariable id: String
    ) = updateUser(id, modifyDto)

    @LoginRequired
    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: String): CommonResponse<Nothing> {
        val user = userRepository.findByIdOrNull(id)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "用户不存在")
        userRepository.delete(user)
        return CommonResponse.ok(null, "删除成功")
    }

    /**
     * 校验用户
     */
    private fun checkUserValid(user: User) {
        val target = userRepository.findByUsername(user.username)
        if (target != null && target.id != user.id)
            throw ResponseStatusException(HttpStatus.ACCEPTED, "用户名已存在")
    }

    /**
     * 通用的用户更新函数
     */
    fun updateUser(id: String, modifyDto: UserModifyDto): CommonResponse<UserViewDto> {
        val user = userRepository.findByIdOrNull(id)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "用户不存在")
        userMapper.partialUpdate(modifyDto, user)
        checkUserValid(user)
        userRepository.save(user)
        return CommonResponse.ok(userMapper.toDto(user), "更新成功")
    }
}
