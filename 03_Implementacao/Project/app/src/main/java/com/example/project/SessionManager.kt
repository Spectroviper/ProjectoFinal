package com.example.project
import com.example.generated.SignInQuery
import com.example.generated.type.Person

object SessionManager {
    var currentUser: SignInQuery.SignIn?? = null
        private set

    fun setCurrentUser(user: SignInQuery.SignIn?) {
        currentUser = user
    }

    fun clearSession() {
        currentUser = null
    }
}
