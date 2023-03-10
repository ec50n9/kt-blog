package com.example.blog.config

import com.example.blog.domain.Article
import com.example.blog.domain.Permission
import com.example.blog.domain.Role
import com.example.blog.domain.User
import com.example.blog.repo.ArticleRepository
import com.example.blog.repo.PermissionRepository
import com.example.blog.repo.RoleRepository
import com.example.blog.repo.UserRepository
import com.example.blog.utils.MessageDigestUtils
import org.springframework.boot.ApplicationRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.transaction.annotation.Transactional
import java.security.MessageDigest

@Configuration
class BlogConfiguration(
    private val userRepository: UserRepository,
    private val roleRepository: RoleRepository,
    private val permissionRepository: PermissionRepository,
    private val articleRepository: ArticleRepository,
) {

    @Transactional
    fun createRoleAndPermission() {
        val permHello = Permission(
            name = "Hello",
            url = "/api/hello",
            method = "GET"
        )
        permissionRepository.save(permHello)

        val roleAdmin = Role(
            name = "Admin",
        )
        roleRepository.save(roleAdmin)
        roleAdmin.permissions.add(permHello)
        roleRepository.save(roleAdmin)
    }

    @Bean
    fun databaseInitializer(
        userRepository: UserRepository,
        roleRepository: RoleRepository,
        permissionRepository: PermissionRepository,
        articleRepository: ArticleRepository,
    ) = ApplicationRunner {
        MessageDigest.getInstance("MD5")

        val permHello = Permission(
            name = "test",
            url = "/api/hello",
            method = "GET"
        )

        val roleAdmin = Role(
            name = "admin",
        )
        roleAdmin.permissions.add(permHello)

        val ec50n9 = User(
            username = "ec50n9",
            password = MessageDigestUtils.md5("1234"),
            firstname = "Gaosong",
            lastname = "Liang"
        )
        ec50n9.roles.add(roleAdmin)
        userRepository.save(ec50n9)
        articleRepository.save(
            Article(
                title = "Reactor Bismuth is out",
                headline = "Lorem ipsum",
                content = "dolor sit amet",
                author = ec50n9
            )
        )
        articleRepository.save(
            Article(
                title = "Vue ??? diff ??????????????????",
                headline = "?????????????????????????????? diff ????????????????????????????????????",
                content = """# ?????????????????????
                    |## ??????????????????
                    |* aaaaa
                    |* bbbbb
                    |* ccccc
                    |> ???????????????????????????
                    |```shell
                    |echo 'hello, wrold'
                    |```
                """.trimMargin(),
                author = ec50n9
            )
        )
    }
}