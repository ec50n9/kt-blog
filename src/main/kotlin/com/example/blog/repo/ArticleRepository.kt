package com.example.blog.repo

import com.example.blog.domain.Article
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository

interface ArticleRepository : JpaRepository<Article, String> {
    fun findAllByOrderByAddedAtDesc(): Iterable<Article>
    fun findByTitleLikeIgnoreCase(title: String, pageable: Pageable): Page<Article>
    fun findByTitleLikeIgnoreCaseAndAuthor_UsernameLikeIgnoreCase(
        title: String?,
        username: String?,
        pageable: Pageable
    ): Page<Article>
}