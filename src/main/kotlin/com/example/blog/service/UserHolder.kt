package com.example.blog.service

import com.example.blog.domain.User

object UserHolder {
    val threadLocal = ThreadLocal<User>()
}