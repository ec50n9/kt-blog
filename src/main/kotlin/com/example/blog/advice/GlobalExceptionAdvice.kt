package com.example.blog.advice

import com.example.blog.domain.CommonResponse
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.MissingServletRequestParameterException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.server.ResponseStatusException

@RestControllerAdvice
class GlobalExceptionAdvice {

    private val log = LoggerFactory.getLogger(RestControllerAdvice::class.java)

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleNotValidException(e: MethodArgumentNotValidException): CommonResponse<Nothing> {
        val errors = e.fieldErrors
        val msgs = errors.map { it.defaultMessage }
        return CommonResponse.fail(msgs.joinToString(), HttpStatus.BAD_REQUEST)
    }

    /**
     * 处理带有状态码的报错
     */
    @ExceptionHandler(ResponseStatusException::class)
    fun handleResponseStatusException(e: ResponseStatusException) =
        CommonResponse.fail(e.reason ?: "", e.status)

    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun handleHttpMessageNotReadableException(e: HttpMessageNotReadableException) =
        CommonResponse.fail("请求体格式有误")

    @ExceptionHandler(MissingServletRequestParameterException::class)
    fun handleMissingParameterException(e: MissingServletRequestParameterException) =
        CommonResponse.fail("缺失字段: ${e.parameterName}", HttpStatus.BAD_REQUEST)

    @ExceptionHandler(Exception::class)
    fun handleException(e: Exception): CommonResponse<Nothing> {
        log.error("未处理的错误:")
        e.printStackTrace()
        return CommonResponse.fail("服务器内部错误")
    }
}