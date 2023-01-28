package com.example.blog.service

import com.example.blog.repo.PermissionRepository
import com.example.blog.repo.RoleRepository
import org.springframework.stereotype.Service

@Service
class PermissionService(
    private val roleRepository: RoleRepository,
    private val permissionRepository: PermissionRepository
) {

    fun checkPermission(method: String, requestUrl: String, permissions: Array<out String>): Boolean {
        return false
    }
}