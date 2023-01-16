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

    fun generateToken(username: String): String =
        JWT.create()
            .withExpiresAt(Date(System.currentTimeMillis() + expiration))
            .withAudience(username)
            .sign(Algorithm.HMAC256(username + secret))

    fun decodeTokenAndGetUsername(token: String): String? {
        return try {
            // 解析token
            val jwt = JWT.decode(token)
            val username = jwt.audience[0]

            // 验证token
            val verifier = JWT.require(Algorithm.HMAC256(username + secret)).build()
            verifier.verify(jwt)
            username
        } catch (e: JWTDecodeException) {
            log.error("token 解析错误: $e")
            null
        } catch (e: JWTVerificationException) {
            log.error("token 验证错误: $e")
            null
        }
    }
}