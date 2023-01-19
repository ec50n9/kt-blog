package com.example.blog.domain.mapper

import com.example.blog.domain.User
import com.example.blog.domain.dto.UserModifyDto
import com.example.blog.domain.dto.UserViewDto
import org.mapstruct.*
import org.mapstruct.factory.Mappers

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
abstract class UserMapper {

    abstract fun toDto(user: User): UserViewDto

    abstract fun toDto(users: Iterable<User>): Iterable<UserViewDto>

    abstract fun toEntity(userModifyDto: UserModifyDto): User

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    abstract fun partialUpdate(userModifyDto: UserModifyDto, @MappingTarget user: User): User
}