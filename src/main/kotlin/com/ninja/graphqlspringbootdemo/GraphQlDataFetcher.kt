package com.ninja.graphqlspringbootdemo

import graphql.schema.DataFetcher
import org.springframework.stereotype.Component
import java.time.LocalDate

@Component
class GraphQlDataFetcher {

    val find = DataFetcher<Member> { env ->
        Member(
                id = "1",
                name = "Bob"
        )
    }

    val findByName = DataFetcher<List<Member>> { env ->
        listOf(
                Member(
                        id = "5",
                        name = "Tom"
                ),
                Member(
                        id = "7",
                        name = "John",
                        histories = listOf(
                                History(
                                        date = LocalDate.parse("2013-09-23"),
                                        event = "born"
                                ),
                                History(
                                        date = LocalDate.parse("2011-04-04"),
                                        event = "born"
                                )
                        )
                )
        )
    }

    val getHistories = DataFetcher<List<History>> { env ->
        listOf(
                History(
                        date = LocalDate.parse("2013-09-23"),
                        event = "born"
                ),
                History(
                        date = LocalDate.parse("2011-04-04"),
                        event = "born"
                )
        )
    }
}
