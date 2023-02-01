package com.example.blog.utils

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.JWTDecodeException
import com.auth0.jwt.exceptions.JWTVerificationException
import org.slf4j.LoggerFactory
import java.util.*

object JWTUtils {

    private val log = LoggerFactory.getLogger("JWTUtils")

    private const val secret = "ec50n9"
    private const val expiration: Long = 30 * 60 * 1000

    fun generateToken(userId: String): String =
        JWT.create()
            .withExpiresAt(Date(System.currentTimeMillis() + expiration))
            .withAudience(userId)
            .sign(Algorithm.HMAC256(userId + secret))

    fun decodeTokenAndGetUserId(token: String): String? {
        return try {
            // 解析token
            val jwt = JWT.decode(token)
            val userId = jwt.audience[0]

            // 验证token
            val verifier = JWT.require(Algorithm.HMAC256(userId + secret)).build()
            verifier.verify(jwt)
            userId
        } catch (e: JWTDecodeException) {
            log.error("token 解析错误: $e")
            null
        } catch (e: JWTVerificationException) {
            log.error("token 验证错误: $e")
            null
        }
    }
}