package com.example.blog

import com.example.blog.domain.Article
import com.example.blog.domain.User
import com.example.blog.repo.ArticleRepository
import com.example.blog.repo.UserRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.springframework.data.repository.findByIdOrNull

@DataJpaTest
class RepositoriesTests @Autowired constructor(
    val entityManager: TestEntityManager,
    val userRepository: UserRepository,
    val articleRepository: ArticleRepository
) {

    @Test
    fun `When findByIdOrNull then return Article`() {
        val juergen = User(username = "springjuergen", password = "1234", firstname = "Juergen", lastname = "Hoeller")
        entityManager.persist(juergen)
        val article = Article("Spring Framework 5.0 goes GA", "Dear Spring community ...", "Lorem ipsum", juergen)
        entityManager.persist(article)
        entityManager.flush()
        val found = articleRepository.findByIdOrNull(article.id!!)
        assertThat(found).isEqualTo(article)
    }

    @Test
    fun `When findByLogin then return User`() {
        val juergen = User(username = "springjuergen", password = "1234", firstname = "Juergen", lastname = "Hoeller")
        entityManager.persist(juergen)
        entityManager.flush()
        val user = userRepository.findByUsername(juergen.username)
        assertThat(user).isEqualTo(juergen)
    }
}