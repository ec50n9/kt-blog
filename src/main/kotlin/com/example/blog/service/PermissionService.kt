package com.example.blog.service

import com.example.blog.annotation.PermissionCheck
import com.example.blog.annotation.RoleCheck
import com.example.blog.repo.UserRepository
import org.slf4j.LoggerFactory
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

/**
 * 权限业务
 */
@Service
class PermissionService(
    private val userRepository: UserRepository,
    private val authService: AuthService
) {

    private val logger = LoggerFactory.getLogger(PermissionService::class.java)

    /**
     * 检查权限是否匹配
     */
    fun checkPermission(
        method: String,
        requestUrl: String,
        permissionCheck: PermissionCheck,
    ): Boolean {
        val currentUser = authService.getCurrentUser()
        val user = userRepository.findByIdOrNull(currentUser.id!!)!!

        val userPermissions = user.roles.flatMap { it.permissions }.map { it.name }
        val requirePermissions = permissionCheck.value
        return if (permissionCheck.requireAll)
            requirePermissions.none { it !in userPermissions }
        else
            requirePermissions.any { it in userPermissions }
    }

    /**
     * 检查角色是否匹配
     */
    fun checkRole(roleCheck: RoleCheck): Boolean {
        val currentUser = authService.getCurrentUser()
        val user = userRepository.findByIdOrNull(currentUser.id!!)!!

        val userRoles = user.roles.map { it.name }
        val requireRoles = roleCheck.value
        return if (roleCheck.requireAll)
            requireRoles.none { it !in userRoles }
        else
            requireRoles.any { it in userRoles }
    }
}