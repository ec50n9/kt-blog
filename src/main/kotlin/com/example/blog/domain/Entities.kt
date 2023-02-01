package com.example.blog.domain

import com.example.blog.utils.NanoIdUtils
import org.hibernate.annotations.GenericGenerator
import org.hibernate.engine.spi.SharedSessionContractImplementor
import org.hibernate.id.IdentifierGenerator
import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedBy
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.io.Serializable
import java.time.LocalDateTime
import javax.persistence.*

class NanoIdGenerator : IdentifierGenerator {
    override fun generate(session: SharedSessionContractImplementor?, `object`: Any?): Serializable {
        return NanoIdUtils.randomNanoId()
    }
}

@MappedSuperclass
abstract class AbstractAuditable(
    @Id
    @GenericGenerator(name = "nanoIdGenerator", strategy = "com.example.blog.domain.NanoIdGenerator")
    @GeneratedValue(generator = "nanoIdGenerator", strategy = GenerationType.SEQUENCE)
    var id: String? = null,

    @CreatedBy
    @ManyToOne
    var createdBy: User? = null,

    @CreatedDate
    var createdDate: LocalDateTime? = null,

    @LastModifiedBy
    @ManyToOne
    var lastModifiedBy: User? = null,

    @LastModifiedDate
    var lastModifiedDate: LocalDateTime? = null
)

@Entity
@EntityListeners(value = [AuditingEntityListener::class])
class Article(
    var title: String,
    var headline: String,
    var content: String,
    @ManyToOne var author: User,
    var addedAt: LocalDateTime = LocalDateTime.now(),
) : AbstractAuditable()

@Entity
@EntityListeners(value = [AuditingEntityListener::class])
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
    var roles: MutableSet<Role> = mutableSetOf(),
) : AbstractAuditable()

@Entity
class Role(
    var name: String,

    @ManyToMany(mappedBy = "roles", targetEntity = User::class)
    var users: MutableSet<User> = mutableSetOf(),

    @ManyToMany(cascade = [CascadeType.ALL], targetEntity = Permission::class)
    @JoinTable(
        name = "role_permission",
        joinColumns = [JoinColumn(name = "role_id")],
        inverseJoinColumns = [JoinColumn(name = "permission_id")]
    )
    var permissions: MutableSet<Permission> = mutableSetOf(),

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
    var roles: MutableSet<Role> = mutableSetOf(),

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "nanoIdGenerator")
    var id: String? = null
)