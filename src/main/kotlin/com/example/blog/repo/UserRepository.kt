package com.example.blog.repo

import com.example.blog.domain.User
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository

interface UserRepository : CrudRepository<User, Long> {
    fun findByUsername(username: String): User?

    @Query("select (count(u) > 0) from User u where u.username = ?1")
    fun existsByUsername(username: String): Boolean
}