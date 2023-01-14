package com.example.blog

import com.example.blog.utils.MessageDigestUtils
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class MessageDigestUtilsTest {

    @Test
    fun md5() {
        val result = MessageDigestUtils.md5("1234")
        assertThat(result).hasSize(32)
    }

    @Test
    fun sha1() {
        val result = MessageDigestUtils.sha1("1234")
        assertThat(result).hasSize(40)
    }

    @Test
    fun sha256() {
        val result = MessageDigestUtils.sha256("1234")
        assertThat(result).hasSize(64)
    }
}