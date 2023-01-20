package com.example.blog.domain

import com.example.blog.utils.NanoIdUtils
import org.hibernate.annotations.GenericGenerator
import org.hibernate.engine.spi.SharedSessionContractImplementor
import org.hibernate.id.IdentifierGenerator
import java.io.Serializable
import java.time.LocalDateTime
import javax.persistence.*

class NanoIdGenerator : IdentifierGenerator {
    override fun generate(session: SharedSessionContractImplementor?, `object`: Any?): Serializable {
        return NanoIdUtils.randomNanoId()
    }
}

@Entity
class Article(
    var title: String,
    var headline: String,
    var content: String,
    @ManyToOne var author: User,
    var addedAt: LocalDateTime = LocalDateTime.now(),

    @Id
    @GenericGenerator(name = "nanoIdGenerator", strategy = "com.example.blog.domain.NanoIdGenerator")
    @GeneratedValue(generator = "nanoIdGenerator", strategy = GenerationType.SEQUENCE)
    var id: String? = null
)

@Entity
class User(
    @Column(unique = true) var username: String,
    var password: String,
    var token: String? = null,
    var firstname: String,
    var lastname: String,
    var description: String? = null,
    @Id @GeneratedValue var id: Long? = null
)