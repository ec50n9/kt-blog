package com.example.blog

import org.springframework.http.HttpStatus
import javax.validation.constraints.NotBlank

data class CommonResult<T>(
    var code: Int,
    var data: T?,
    var msg: String?,
) {
    companion object {
        fun <T> ok(data: T? = null, msg: String = "请求成功", status: HttpStatus = HttpStatus.OK) =
            CommonResult(status.value(), data, msg)

        fun fail(msg: String = "请求失败", status: HttpStatus = HttpStatus.INTERNAL_SERVER_ERROR) =
            CommonResult(status.value(), null, msg)
    }
}

data class UserAuthVO(
    @field:NotBlank(message = "用户名不能为空")
    val username: String?,
    @field:NotBlank(message = "密码不能为空")
    val password: String?
)

class UserVO(user: User) {
    var username: String = user.username
    var firstname: String = user.firstname
    var lastname: String = user.lastname
    var description: String? = user.description
    var id: Long? = user.id
}
