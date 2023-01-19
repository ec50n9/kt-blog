package com.example.blog.domain

import org.springframework.http.HttpStatus

data class CommonResponse<T>(
    var code: Int,
    var data: T?,
    var msg: String?,
) {
    companion object {
        fun <T> ok(data: T? = null, msg: String = "请求成功", status: HttpStatus = HttpStatus.OK) =
            CommonResponse(status.value(), data, msg)

        fun fail(msg: String = "请求失败", status: HttpStatus = HttpStatus.INTERNAL_SERVER_ERROR) =
            CommonResponse(status.value(), null, msg)
    }
}
