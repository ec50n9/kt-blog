package com.example.blog.utils

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import java.util.*

object JWTUtils {

    val SALT = "ec50n9"
    val EXPIRE_TIME: Long = 30 * 60 * 1000

    fun generateToken(username: String) =
        JWT.create()
            .withExpiresAt(Date(System.currentTimeMillis() + EXPIRE_TIME))
            .withAudience(username)
            .sign(Algorithm.HMAC256(username + SALT))
}