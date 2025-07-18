package com.example.projecttest.services.graphql

import com.apollographql.apollo3.ApolloClient
import com.example.projecttest.AppConfig


object ApolloClientInstance {
    val apolloClient = ApolloClient.Builder()
        .serverUrl(AppConfig.DOMAIN + AppConfig.PORT + AppConfig.GRAPHQL_SERVICE)
        .build()
}
