package com.example.blog.domain.dto

import java.io.Serializable
import java.time.LocalDateTime
import javax.validation.constraints.NotBlank

data class ArticleViewDto(
    var title: String? = null,
    var headline: String? = null,
    var content: String? = null,
    var author: UserViewDto? = null,
    var addedAt: LocalDateTime = LocalDateTime.now(),
    var createdBy: String? = null,
    var createdDate: LocalDateTime? = null,
    var lastModifiedBy: String? = null,
    var lastModifiedDate: LocalDateTime? = null,
    var id: String? = null
) : Serializable

data class ArticleModifyDto(
    @field:NotBlank(message = "标题不能为空")
    var title: String? = null,
    @field:NotBlank(message = "摘要不能为空")
    var headline: String? = null,
    @field:NotBlank(message = "内容不能为空")
    var content: String? = null,
    var addedAt: LocalDateTime = LocalDateTime.now()
)
