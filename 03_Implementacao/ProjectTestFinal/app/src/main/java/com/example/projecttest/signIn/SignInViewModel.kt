package com.example.projecttest.signIn;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.projecttest.services.mock.AppDomain
import com.example.projecttest.data.mock.AppMock
import com.example.projecttest.MyId
import com.example.projecttest.data.model.User
import com.example.projecttest.data.model.UserGame
import com.example.projecttest.services.ServiceLocator

import com.google.firebase.auth.FirebaseAuth;

class SignInViewModel : ViewModel() {

    private val firebaseAuth = FirebaseAuth.getInstance()

    private val userService = ServiceLocator.userService

    private val _signInState = MutableLiveData<SignInState>()
    val signInState: LiveData<SignInState> get() = _signInState

    fun signIn(email: String, password: String) {
        if (email.isEmpty() || password.isEmpty()) {
            _signInState.value = SignInState.Error("Empty Fields Are not Allowed !!")
            return
        }

        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("SignInViewModel", "Firebase sign-in successful")

                    val result = userService.logIn(email)
                    result.observeForever { user ->
                        if (user?.email == email) {
                            Log.d("SignInViewModel", "${user}")
                            MyId.user = user
                            Log.d("SignInViewModel", "${MyId.user}")
                            _signInState.value = SignInState.Success
                        } else {
                            _signInState.value = SignInState.Error("User not found")
                        }
                    }
                } else {
                    Log.e("SignInViewModel", "Sign-in failed", task.exception)
                    _signInState.value =
                        SignInState.Error(task.exception?.message ?: "Authentication failed")
                }
            }
    }
}

sealed class SignInState {
    object Success : SignInState()
    data class Error(val message: String) : SignInState()
}
