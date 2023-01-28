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

    @ManyToMany(cascade = [CascadeType.ALL], targetEntity = Role::class)
    @JoinTable(
        name = "user_role",
        joinColumns = [JoinColumn(name = "user_id")],
        inverseJoinColumns = [JoinColumn(name = "role_id")]
    )
    var roles: Set<Role> = hashSetOf(),

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "nanoIdGenerator")
    var id: String? = null
)

@Entity
class Role(
    var name: String,

    @ManyToMany(mappedBy = "roles", targetEntity = User::class)
    var users: Set<User> = hashSetOf(),

    @ManyToMany(cascade = [CascadeType.ALL], targetEntity = Permission::class)
    @JoinTable(
        name = "role_permission",
        joinColumns = [JoinColumn(name = "role_id")],
        inverseJoinColumns = [JoinColumn(name = "permission_id")]
    )
    var permissions: Set<Permission> = hashSetOf(),

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "nanoIdGenerator")
    var id: String? = null
)

@Entity
class Permission(
    var name: String,
    var url: String,
    var method: String,

    @ManyToMany(mappedBy = "permissions", targetEntity = Role::class)
    var roles: Set<Role> = hashSetOf(),

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "nanoIdGenerator")
    var id: String? = null
)