package com.example.blog.controller

import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException
import java.util.*

abstract class BaseController {

    /**
     * 检查页码和大小是否合理
     */
    fun isValidPage(page: Int, size: Int) =
        page * size in 1..10000

    /**
     * 检查属性是否存在于类中
     */
    inline fun <reified T> isMember(name: String) =
        T::class.members.any { name == it.name }

    /**
     * 检查排序方式
     */
    fun isValidOrder(value: String) =
        value.uppercase(Locale.US) in arrayOf("DESC", "ASC")

    inline fun <reified T> createPageRequest(page: Int, size: Int, orderBy: String, sortBy: String): PageRequest {
        if (!isValidPage(page, size))
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "页码超出限制")
        if (!isMember<T>(sortBy))
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "$sortBy 字段不存在")
        if (!isValidOrder(orderBy))
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "排序只支持 desc 和 asc")

        return PageRequest.of(
            page - 1,
            size,
            Sort.by(
                Sort.Direction.valueOf(orderBy.uppercase()),
                sortBy
            )
        )
    }
}
