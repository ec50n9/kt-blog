package com.example.blog.repo

import com.example.blog.domain.Article
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.CrudRepository

interface ArticleRepository : CrudRepository<Article, String> {
    fun findBySlug(slug: String): Article?
    fun findAllByOrderByAddedAtDesc(): Iterable<Article>
    fun findAll(pageable: Pageable): List<Article>
}