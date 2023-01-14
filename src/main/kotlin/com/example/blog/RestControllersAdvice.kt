package com.example.blog

import org.springframework.http.HttpStatus
import org.springframework.web.bind.MissingServletRequestParameterException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class RestControllersAdvice {

    @ExceptionHandler(MissingServletRequestParameterException::class)
    fun handleMissingParameterException(e: MissingServletRequestParameterException) =
        CommonResult.fail("缺失字段: ${e.parameterName}", HttpStatus.BAD_REQUEST)

    @ExceptionHandler(Exception::class)
    fun handleException(e: Exception) {
        println("未处理的错误: $e")
    }
}