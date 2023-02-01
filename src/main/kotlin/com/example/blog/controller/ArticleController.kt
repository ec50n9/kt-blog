package com.example.blog.controller

import com.example.blog.annotation.CurrentUser
import com.example.blog.domain.Article
import com.example.blog.domain.CommonResponse
import com.example.blog.domain.User
import com.example.blog.domain.dto.ArticleModifyDto
import com.example.blog.domain.dto.ArticleViewDto
import com.example.blog.domain.mapper.ArticleMapper
import com.example.blog.repo.ArticleRepository
import org.slf4j.LoggerFactory
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import java.util.*

@RestController
@RequestMapping("/api/articles")
class ArticleController(
    private val articleRepository: ArticleRepository,
    private val articleMapper: ArticleMapper
) : BaseController() {

    val logger = LoggerFactory.getLogger(ArticleController::class.java)

    @GetMapping
    fun findAll(
        @RequestParam(defaultValue = "1") page: Int,
        @RequestParam(defaultValue = "10") size: Int,
        @RequestParam(defaultValue = "addedAt") sortBy: String,
        @RequestParam(defaultValue = "desc") orderBy: String,
        @RequestParam(defaultValue = "") title: String,
        @RequestParam(defaultValue = "") author: String,
    ): Iterable<ArticleViewDto> {
        val pageRequest = createPageRequest<Article>(page, size, orderBy, sortBy)
        // 模糊查询
        val articleList =
            articleRepository.findByTitleLikeIgnoreCaseAndAuthor_UsernameLikeIgnoreCase(
                "%$title%",
                "%$author%",
                pageRequest
            )
        return articleMapper.toDto(articleList)
    }

    @GetMapping("/{id}")
    fun findOne(@PathVariable id: String): ArticleViewDto {
        val article = articleRepository.findByIdOrNull(id)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "文章不存在")
        return articleMapper.toDto(article)
    }

    @PostMapping
    fun create(
        @CurrentUser user: User,
        @Validated @RequestBody modifyDto: ArticleModifyDto
    ): CommonResponse<ArticleViewDto> {
        val entity = articleMapper.toEntity(modifyDto)

        // 设置作者
        entity.author = user

        val article = articleRepository.save(entity)
        return CommonResponse.ok(articleMapper.toDto(article), "创建成功", HttpStatus.CREATED)
    }


    fun updateArticle(
        user: User,
        id: String,
        modifyDto: ArticleModifyDto
    ): CommonResponse<ArticleViewDto> {
        val article =
            articleRepository.findByIdOrNull(id) ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "文章不存在")

        // 校验权限
        if (article.author.id != user.id) throw ResponseStatusException(HttpStatus.FORBIDDEN, "权限不足")

        articleMapper.partialUpdate(modifyDto, article)
        articleRepository.save(article)
        return CommonResponse.ok(articleMapper.toDto(article), "更新成功")
    }

    @PutMapping("/{id}")
    fun update(
        @CurrentUser user: User,
        @Validated @RequestBody modifyDto: ArticleModifyDto,
        @PathVariable id: String
    ) = updateArticle(user, id, modifyDto)

    @PatchMapping("/{id}")
    fun patch(
        @CurrentUser user: User,
        @PathVariable id: String,
        @RequestBody modifyDto: ArticleModifyDto
    ) = updateArticle(user, id, modifyDto)

    @DeleteMapping("/{id}")
    fun delete(@CurrentUser user: User, @PathVariable id: String): CommonResponse<Nothing> {
        val article =
            articleRepository.findByIdOrNull(id) ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "文章不存在")

        // 校验权限
        if (article.author.id != user.id) throw ResponseStatusException(HttpStatus.FORBIDDEN, "权限不足")

        articleRepository.delete(article)
        return CommonResponse.ok(null, "删除成功")
    }
}
