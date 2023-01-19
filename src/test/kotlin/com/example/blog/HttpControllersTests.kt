package com.example.blog

import com.example.blog.domain.Article
import com.example.blog.domain.User
import com.example.blog.repo.ArticleRepository
import com.example.blog.repo.UserRepository
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@WebMvcTest
class HttpControllersTests(@Autowired val mockMvc: MockMvc) {

    @MockkBean
    private lateinit var userRepository: UserRepository

    @MockkBean
    private lateinit var articleRepository: ArticleRepository

    @Test
    fun `List articles`() {
        val juergen = User(username = "springjuergen", password = "1234", firstname = "Juergen", lastname = "Hoeller")
        val spring5Article =
            Article("Spring Framework 5.0 goes GA", "Dear Spring community ...", "Lorem ipsum", juergen)
        val spring43Article = Article("Spring Framework 4.3 goes GA", "Dear Spring community ...", "Lorem ipsum", juergen)
        every { articleRepository.findAllByOrderByAddedAtDesc() } returns listOf(spring5Article, spring43Article)
        mockMvc.perform(get("/api/article/").accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("\$.[0].author.login").value(juergen.username))
            .andExpect(jsonPath("\$.[0].slug").value(spring5Article.slug))
            .andExpect(jsonPath("\$.[1].author.login").value(juergen.username))
            .andExpect(jsonPath("\$.[1].slug").value(spring43Article.slug))
    }

    @Test
    fun `List users`() {
        val juergen = User(username = "springjuergen", password = "1234", firstname = "Juergen", lastname = "Hoeller")
        val smaldini = User(username = "smaldini", password = "1234", firstname = "Stéphane", lastname = "Maldini")
        every { userRepository.findAll() } returns listOf(juergen, smaldini)
        mockMvc.perform(get("/api/users").accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("\$.[0].username").value(juergen.username))
            .andExpect(jsonPath("\$.[1].username").value(smaldini.username))
    }
}