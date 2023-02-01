package com.example.blog.domain.dto

import java.io.Serializable
import java.time.LocalDateTime
import javax.validation.constraints.NotBlank

data class UserViewDto(
    var username: String? = null,
    var firstname: String? = null,
    var lastname: String? = null,
    var description: String? = null,
    var createdBy: String? = null,
    var createdDate: LocalDateTime? = null,
    var lastModifiedBy: String? = null,
    var lastModifiedDate: LocalDateTime? = null,
    var id: String? = null
) : Serializable

data class UserModifyDto(
    @field:NotBlank(message = "用户名不能为空")
    var username: String? = null,
    @field:NotBlank(message = "密码不能为空")
    var password: String? = null,
    @field:NotBlank(message = "名字不能为空")
    var firstname: String? = null,
    @field:NotBlank(message = "姓不能为空")
    var lastname: String? = null,
    var description: String? = null
) : Serializable

data class UserAuthDto(
    @field:NotBlank(message = "用户名不能为空")
    var username: String? = null,
    @field:NotBlank(message = "密码不能为空")
    var password: String? = null
) : Serializable
