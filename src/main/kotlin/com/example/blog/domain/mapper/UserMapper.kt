package com.example.blog.domain.mapper

import com.example.blog.domain.User
import com.example.blog.domain.dto.UserModifyDto
import com.example.blog.domain.dto.UserViewDto
import org.mapstruct.*

@Mapper(
    componentModel = "spring",
    unmappedSourcePolicy = ReportingPolicy.IGNORE,
    unmappedTargetPolicy = ReportingPolicy.IGNORE
)
abstract class UserMapper {

    fun getUsername(user: User?) = user?.username

    abstract fun toDto(user: User): UserViewDto

    abstract fun toDto(users: Iterable<User>): Iterable<UserViewDto>

    @Mapping(target = "roles", ignore = true)
    abstract fun toEntity(userModifyDto: UserModifyDto): User

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    abstract fun partialUpdate(userModifyDto: UserModifyDto, @MappingTarget user: User): User
}