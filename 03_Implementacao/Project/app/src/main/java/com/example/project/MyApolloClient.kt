package com.example.project

import com.apollographql.apollo3.ApolloClient

object MyApolloClient {
    private const val BASE_URL = "http://10.0.2.2:3001/graphql"  // Locally

    val instance: ApolloClient by lazy {
        ApolloClient.Builder()
            .serverUrl(BASE_URL)
            .build()
    }
}