package com.example.projecttest.services

import com.example.projecttest.services.graphql.APIService
import com.example.projecttest.services.mock.AppDomain

object ServiceLocator {
    //private val apiService = AppDomain()
    private val apiService = APIService()
    val userService: UserService = DataSource(apiService)
}