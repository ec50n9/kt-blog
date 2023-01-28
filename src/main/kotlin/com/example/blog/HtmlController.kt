package com.example.blog

import com.example.blog.config.BlogProperties
import com.example.blog.domain.Article
import com.example.blog.domain.User
import com.example.blog.repo.ArticleRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.ui.set
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.server.ResponseStatusException

@Controller
class HtmlController(
    private val repository: ArticleRepository,
    private val properties: BlogProperties
) {

    data class RenderedArticle(
        val id: String,
        val title: String,
        val headline: String,
        val content: String,
        val author: User,
        val addedAt: String
    )

    fun Article.render() = RenderedArticle(
        id ?: "",
        title,
        headline,
        content,
        author,
        addedAt.format()
    )

    @GetMapping("/")
    fun blog(model: Model): String {
        model["title"] = properties.title
        model["banner"] = properties.banner
        model["articles"] = repository.findAllByOrderByAddedAtDesc().map { it.render() }
        return "blog"
    }

    @GetMapping("/article/{id}")
    fun article(@PathVariable id: String, model: Model): String {
        val article = repository
            .findByIdOrNull(id)
            ?.render()
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "This article dose not exist")
        model["title"] = article.title
        model["article"] = article
        return "article"
    }
}