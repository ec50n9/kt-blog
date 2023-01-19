package com.example.blog.controller

import com.example.blog.domain.CommonResponse
import com.example.blog.domain.dto.ArticleModifyDto
import com.example.blog.domain.dto.ArticleViewDto
import com.example.blog.domain.mapper.ArticleMapper
import com.example.blog.repo.ArticleRepository
import org.springframework.http.HttpStatus
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("/api/articles")
class ArticleController(
    private val articleRepository: ArticleRepository,
    private val articleMapper: ArticleMapper
) {

    @GetMapping
    fun findAll() = articleMapper.toDto(articleRepository.findAllByOrderByAddedAtDesc())

    @GetMapping("/{slug}")
    fun findOne(@PathVariable slug: String): ArticleViewDto {
        val article = articleRepository.findBySlug(slug) ?: throw ResponseStatusException(
            HttpStatus.NOT_FOUND,
            "文章不存在"
        )
        return articleMapper.toDto(article)
    }

    @PostMapping
    fun create(@Validated @RequestBody articleModifyDto: ArticleModifyDto): CommonResponse<ArticleViewDto> {
        val entity = articleMapper.toEntity(articleModifyDto)
        val article = articleRepository.save(entity)
        return CommonResponse.ok(articleMapper.toDto(article), "创建成功", HttpStatus.CREATED)
    }
}
