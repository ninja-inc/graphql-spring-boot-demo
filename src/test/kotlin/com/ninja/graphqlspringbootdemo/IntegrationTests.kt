package com.ninja.graphqlspringbootdemo

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.http.MediaType
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono
import java.lang.RuntimeException

@ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class IntegrationTests {
    @LocalServerPort
    private var port: Int = -1
    private lateinit var webClient: WebClient
    private val mapper = ObjectMapper()

    @BeforeEach
    fun setup() {
        webClient = WebClient.create("http://localhost:$port")
    }

    @Test
    fun simpleTest() {
        val response: Map<String, Any> = webClient.post()
                .uri("/graphql")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromObject("""
                    {
                        "query": "{findByName(name: \"Tom\") {id name histories{date event}} }"
                    }
                """.trimIndent()))
                .retrieve()
                .bodyToMono<Map<String, Any>>()
                .block() ?: throw RuntimeException("response is null")

        val expected: Map<String, Any> = mapper.readValue("""
            {
                "data": {
                    "findByName": [
                        {
                            "id": "5",
                            "name": "Tom",
                            "histories": []
                        },
                        {
                            "id": "7",
                            "name": "John",
                            "histories": [
                                {
                                    "date": "2013-09-23",
                                    "event": "born"
                                },
                                {
                                    "date": "2011-04-04",
                                    "event": "born"
                                }
                            ]
                        }
                    ]
                }
            }
        """.trimIndent())


        assertThat(response).isEqualTo(expected)
    }
}
