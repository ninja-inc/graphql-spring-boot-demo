package com.ninja.graphqlspringbootdemo

import org.springframework.data.annotation.Id
import java.time.LocalDate

data class Member(
        @Id
        val id: String,
        val name: String,
        val histories: List<History> = listOf()
)

data class History(
        val date: LocalDate,
        val event: String
)
