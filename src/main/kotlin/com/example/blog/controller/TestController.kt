package com.example.blog.controller

import com.example.blog.annotation.LoginRequired
import com.example.blog.annotation.PermissionCheck
import com.example.blog.domain.CommonResponse
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/test")
class TestController {

    @LoginRequired
    @PermissionCheck(["test", "hello"], true)
    @GetMapping("/hello")
    fun hello(): CommonResponse<String> {
        return CommonResponse.ok("hello")
    }
}