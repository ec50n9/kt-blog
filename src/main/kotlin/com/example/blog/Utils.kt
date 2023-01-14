package com.example.blog

import java.security.MessageDigest

/**
 * 加密工具类
 */
object MessageDigestUtils {

    /**
     * md5 加密
     */
    fun md5(str: String): String {
        val digest = MessageDigest.getInstance("MD5")
        val result = digest.digest(str.toByteArray())
        return toHex(result)
    }

    /**
     * 字节数组转16进制
     */
    private fun toHex(byteArray: ByteArray): String {
        val result = with(StringBuilder()) {
            byteArray.forEach {
                val hex = it.toInt() and (0xFF)
                val hexStr = Integer.toHexString(hex)
                if (hexStr.length == 1)
                    this.append("0").append(hexStr)
                else
                    this.append(hexStr)
            }
            this.toString()
        }
        return result
    }

    /**
     * sha1 加密
     */
    fun sha1(str: String): String{
        val digest = MessageDigest.getInstance("SHA-1")
        val result = digest.digest(str.toByteArray())
        return toHex(result)
    }

    /**
     * sha256 加密
     */
    fun sha256(str: String):String{
        val digest = MessageDigest.getInstance("SHA-256")
        val result = digest.digest(str.toByteArray())
        return toHex(result)
    }
}