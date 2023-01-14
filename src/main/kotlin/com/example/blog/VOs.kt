package com.example.blog

import org.springframework.http.HttpStatus

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

class UserVO(user: User) {
    var username: String = user.username
    var firstname: String = user.firstname
    var lastname: String = user.lastname
    var description: String? = user.description
    var id: Long? = user.id
}
