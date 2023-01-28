package com.example.blog.service

import com.example.blog.annotation.PermissionCheck
import com.example.blog.repo.UserRepository
import org.slf4j.LoggerFactory
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class PermissionService(
    private val userRepository: UserRepository,
) {

    private val logger = LoggerFactory.getLogger(PermissionService::class.java)

    fun checkPermission(
        method: String,
        requestUrl: String,
        permissionCheck: PermissionCheck
    ): Boolean {
        val currentUser = UserHolder.threadLocal.get()
        val user = userRepository.findByIdOrNull(currentUser.id!!)!!

        val userPermissions = user.roles.flatMap { it.permissions }.map { it.name }
        val requirePermissions = permissionCheck.value
        return if (permissionCheck.requireAll)
            requirePermissions.none { it !in userPermissions }
        else
            requirePermissions.any { it in userPermissions }
    }
}