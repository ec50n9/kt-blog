package com.example.blog.domain.mapper

import com.example.blog.domain.Article
import com.example.blog.domain.dto.ArticleModifyDto
import com.example.blog.domain.dto.ArticleViewDto
import com.example.blog.repo.UserRepository
import org.mapstruct.*
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.server.ResponseStatusException

@Mapper(
    componentModel = "spring",
    uses = [UserMapper::class, ArticleMapper.UserTranslator::class]
)
abstract class ArticleMapper {

    @Component
    class UserTranslator(private val userRepository: UserRepository) {
        fun getUserById(id: Long) =
            userRepository.findByIdOrNull(id) ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "用户不存在")
    }

    abstract fun toDto(article: Article): ArticleViewDto

    abstract fun toDto(articleIterable: Iterable<Article>): Iterable<ArticleViewDto>

    @Mapping(source = "authorId", target = "author")
    abstract fun toEntity(articleModifyDto: ArticleModifyDto): Article

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    abstract fun partialUpdate(articleModifyDto: ArticleModifyDto, @MappingTarget article: Article): Article
}
