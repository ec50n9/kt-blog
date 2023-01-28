package com.example.blog.controller

import com.example.blog.annotation.CurrentUser
import com.example.blog.annotation.LoginRequired
import com.example.blog.annotation.PermissionCheck
import com.example.blog.annotation.RoleCheck
import com.example.blog.domain.CommonResponse
import com.example.blog.domain.User
import com.example.blog.domain.dto.UserViewDto
import com.example.blog.domain.mapper.UserMapper
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/test")
class TestController(private val userMapper: UserMapper) {

    @PermissionCheck(["test", "hello"], false)
    @RoleCheck(["admin", "gogog"], true)
    @GetMapping("/hello")
    fun hello(): CommonResponse<String> {
        return CommonResponse.ok("hello")
    }

    @LoginRequired
    @GetMapping("/isLogin")
    fun needLogin(@CurrentUser user: User): CommonResponse<String> {
        return CommonResponse.ok("你已经登录啦！！！")
    }

    @GetMapping("/user")
    fun getUser(@CurrentUser user: User): CommonResponse<UserViewDto> {
        return CommonResponse.ok(userMapper.toDto(user))
    }
}