package com.example.blog.repo

import com.example.blog.domain.User
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<User, String> {

    fun findByUsername(username: String): User?

    fun findByUsernameLikeIgnoreCase(username: String, pageable: Pageable): Page<User>
}
