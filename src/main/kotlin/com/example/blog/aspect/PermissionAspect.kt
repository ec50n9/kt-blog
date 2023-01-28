package com.example.blog.aspect

import com.example.blog.annotation.PermissionCheck
import com.example.blog.annotation.RoleCheck
import com.example.blog.service.PermissionService
import org.aspectj.lang.JoinPoint
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Before
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes
import org.springframework.web.server.ResponseStatusException

@Aspect
@Component
class PermissionAspect(private val permissionService: PermissionService) {

    private val logger = LoggerFactory.getLogger(PermissionAspect::class.java)

    @Before("@annotation(permissionCheck)")
    fun checkPermission(joinPoint: JoinPoint, permissionCheck: PermissionCheck) {
        // 获取请求信息
        val request = (RequestContextHolder.getRequestAttributes() as ServletRequestAttributes).request
        val method = request.method
        val requestUrl = request.requestURI
        val permissions = permissionCheck.value
        logger.info("$method: $requestUrl")
        logger.info("需要权限：${permissions.joinToString()}")
        // 验证是否有权限
        if (!permissionService.checkPermission(method, requestUrl, permissionCheck))
            throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "权限不足")
    }

    @Before("@annotation(roleCheck)")
    fun checkRole(joinPoint: JoinPoint, roleCheck: RoleCheck) {
        // 获取请求信息
        val request = (RequestContextHolder.getRequestAttributes() as ServletRequestAttributes).request
        val method = request.method
        val requestUrl = request.requestURI
        val roles = roleCheck.value
        logger.info("$method: $requestUrl")
        logger.info("需要角色：${roles.joinToString()}")
        // 验证是否有权限
        if (!permissionService.checkRole(roleCheck))
            throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "权限不足")
    }
}