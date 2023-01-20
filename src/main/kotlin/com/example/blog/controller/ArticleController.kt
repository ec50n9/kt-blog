package com.example.blog.controller

import com.example.blog.annotation.LoginRequired
import com.example.blog.domain.CommonResponse
import com.example.blog.domain.User
import com.example.blog.domain.dto.ArticleModifyDto
import com.example.blog.domain.dto.ArticleViewDto
import com.example.blog.domain.mapper.ArticleMapper
import com.example.blog.repo.ArticleRepository
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import javax.servlet.http.HttpServletRequest

@RestController
@RequestMapping("/api/articles")
class ArticleController(
    private val articleRepository: ArticleRepository,
    private val articleMapper: ArticleMapper
) {

    @GetMapping
    fun findAll(
        @RequestParam(defaultValue = "1") page: Int,
        @RequestParam(defaultValue = "10") size: Int
    ): Iterable<ArticleViewDto> {
        val pageRequest = PageRequest.of(page - 1, size, Sort.by("addedAt"))
        val articleList = articleRepository.findAll(pageRequest)
        return articleMapper.toDto(articleList)
    }

    @GetMapping("/{id}")
    fun findOne(@PathVariable id: String): ArticleViewDto {
        val article = articleRepository.findByIdOrNull(id) ?: throw ResponseStatusException(
            HttpStatus.NOT_FOUND,
            "文章不存在"
        )
        return articleMapper.toDto(article)
    }

    @LoginRequired
    @PostMapping
    fun create(
        request: HttpServletRequest,
        @Validated @RequestBody modifyDto: ArticleModifyDto
    ): CommonResponse<ArticleViewDto> {
        val entity = articleMapper.toEntity(modifyDto)

        // 设置作者
        val user = request.getAttribute("user") as User
        entity.author = user

        val article = articleRepository.save(entity)
        return CommonResponse.ok(articleMapper.toDto(article), "创建成功", HttpStatus.CREATED)
    }


    fun updateArticle(
        request: HttpServletRequest,
        id: String,
        modifyDto: ArticleModifyDto
    ): CommonResponse<ArticleViewDto> {
        var article =
            articleRepository.findByIdOrNull(id) ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "文章不存在")

        // 校验权限
        val user = request.getAttribute("user") as User
        if (article.author.id != user.id) throw ResponseStatusException(HttpStatus.FORBIDDEN, "权限不足")

        articleMapper.partialUpdate(modifyDto, article)
        article = articleRepository.save(article)
        return CommonResponse.ok(articleMapper.toDto(article), "更新成功")
    }

    @LoginRequired
    @PutMapping("/{id}")
    fun update(
        request: HttpServletRequest,
        @Validated @RequestBody modifyDto: ArticleModifyDto,
        @PathVariable id: String
    ) = updateArticle(request, id, modifyDto)

    @LoginRequired
    @PatchMapping("/{id}")
    fun patch(
        request: HttpServletRequest,
        @PathVariable id: String,
        @RequestBody modifyDto: ArticleModifyDto
    ) = updateArticle(request, id, modifyDto)

    @LoginRequired
    @DeleteMapping("/{id}")
    fun delete(request: HttpServletRequest, @PathVariable id: String): CommonResponse<Nothing> {
        val article =
            articleRepository.findByIdOrNull(id) ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "文章不存在")

        // 校验权限
        val user = request.getAttribute("user") as User
        if (article.author.id != user.id) throw ResponseStatusException(HttpStatus.FORBIDDEN, "权限不足")

        articleRepository.delete(article)
        return CommonResponse.ok(null, "删除成功")
    }
}
