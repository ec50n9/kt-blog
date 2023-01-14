package com.example.blog

import org.springframework.http.HttpStatus
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.MissingServletRequestParameterException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class RestControllersAdvice {

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleNotValidException(e: MethodArgumentNotValidException): CommonResult<Nothing> {
        val errors = e.fieldErrors
        val msgs = errors.map { it.defaultMessage }
        return CommonResult.fail(msgs.joinToString(), HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(MissingServletRequestParameterException::class)
    fun handleMissingParameterException(e: MissingServletRequestParameterException) =
        CommonResult.fail("缺失字段: ${e.parameterName}", HttpStatus.BAD_REQUEST)

    @ExceptionHandler(Exception::class)
    fun handleException(e: Exception) {
        println("未处理的错误: $e")
    }
}