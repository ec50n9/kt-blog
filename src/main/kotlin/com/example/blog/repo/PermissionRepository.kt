package com.example.blog.repo

import com.example.blog.domain.Permission
import org.springframework.data.jpa.repository.JpaRepository

interface PermissionRepository : JpaRepository<Permission, String> {

}