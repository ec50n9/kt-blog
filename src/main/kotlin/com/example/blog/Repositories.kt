package com.example.blog

import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository

interface ArticleRepository : CrudRepository<Article, Long> {
    fun findBySlug(slug: String): Article?
    fun findAllByOrderByAddedAtDesc(): Iterable<Article>
}

interface UserRepository : CrudRepository<User, Long> {
    fun findByUsername(username: String): User?

    @Query("select (count(u) > 0) from User u where u.username = ?1")
    fun existsByUsername(username: String): Boolean
}