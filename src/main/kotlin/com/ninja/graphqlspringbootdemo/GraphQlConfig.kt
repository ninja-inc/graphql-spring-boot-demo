package com.ninja.graphqlspringbootdemo

import com.coxautodev.graphql.tools.GraphQLQueryResolver
import com.google.common.io.Resources
import graphql.GraphQL
import graphql.schema.GraphQLSchema
import graphql.schema.idl.RuntimeWiring
import graphql.schema.idl.SchemaGenerator
import graphql.schema.idl.SchemaParser
import graphql.schema.idl.TypeRuntimeWiring.newTypeWiring
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import javax.annotation.PostConstruct

@Configuration
class GraphQlConfig(
        val graphQlDataFetcher: GraphQlDataFetcher
) {

    private lateinit var graphQL: GraphQL

    @PostConstruct
    fun init() {
        val url = Resources.getResource("graphql/schema.graphql")
        val sdl = Resources.toString(url, Charsets.UTF_8)
        val graphQLSchema = buildSchema(sdl)
        this.graphQL = GraphQL.newGraphQL(graphQLSchema).build()
    }

    private fun buildSchema(sdl: String): GraphQLSchema {
        val typeRegistry = SchemaParser().parse(sdl)
        val runtimeWiring = buildWiring()
        val schemaGenerator = SchemaGenerator()
        return schemaGenerator.makeExecutableSchema(typeRegistry, runtimeWiring)
    }

    private fun buildWiring(): RuntimeWiring {
        return RuntimeWiring.newRuntimeWiring()
                .type(newTypeWiring("Query")
                        .dataFetcher("find", graphQlDataFetcher.find)
                        .dataFetcher("findByName", graphQlDataFetcher.findByName)
                )
                /*
                .type(newTypeWiring("Member")
                        .dataFetcher("histories", graphQlDataFetcher.getHistories)
                )
                */
                .build()

    }

    @Bean
    fun graphQL(): GraphQL {
        return graphQL
    }
}