package com.example.projecttest.signup

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projecttest.data.model.Message
import com.example.projecttest.services.ServiceLocator
import com.example.projecttest.services.mock.AppDomain
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class SignUpViewModel : ViewModel() {

    private val firebaseAuth = FirebaseAuth.getInstance()
    private val userService = ServiceLocator.userService

    var email by mutableStateOf("")
        private set

    var username by mutableStateOf("")
        private set

    var password by mutableStateOf("")
        private set

    var confirmPassword by mutableStateOf("")
        private set

    var signUpState by mutableStateOf<SignUpState>(SignUpState.Idle)
        private set

    fun onEmailChange(newEmail: String) {
        email = newEmail
    }

    fun onUsernameChange(newUsername: String) {
        username = newUsername
    }

    fun onPasswordChange(newPassword: String) {
        password = newPassword
    }

    fun onConfirmPasswordChange(newConfirmPassword: String) {
        confirmPassword = newConfirmPassword
    }

    fun signUp() {
        if (username.isNotEmpty() && email.isNotEmpty() &&
            password.isNotEmpty() && confirmPassword.isNotEmpty()
        ) {
            if (password == confirmPassword) {
                Log.d("SignUp", "Attempting Firebase sign-up with $email")
                firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val userName = username
                            val userEmail = email

                            Log.d("SignUp", "Firebase sign-up success. Creating user in API...")
                            viewModelScope.launch {
                                Log.d("SignUp", "Calling createUser() with: $userName, $userEmail")
                                val result = userService.createUser(
                                    userName,
                                    userEmail,
                                    "",
                                    ""
                                )

                                result.observeForever { message ->
                                    Log.d("SignUp", "Received response from createUser: $message")

                                    if (message.successfull == true) {
                                        Log.d("SignUp", "User successfully created in API")
                                        signUpState = SignUpState.Success
                                    } else {
                                        Log.e("SignUp", "API failed to create user: ${message.message}")
                                        signUpState = SignUpState.Error(message.message.toString())
                                    }
                                }
                            }
                        } else {
                            val error = task.exception?.message ?: "Unknown Firebase error"
                            Log.e("SignUp", "Firebase sign-up failed: $error")
                            signUpState = SignUpState.Error(error)
                        }
                    }
            } else {
                Log.w("SignUp", "Passwords do not match")
                signUpState = SignUpState.Error("Passwords do not match")
            }
        } else {
            Log.w("SignUp", "Empty fields")
            signUpState = SignUpState.Error("Empty fields are not allowed")
        }
    }
}

sealed class SignUpState {
    object Idle : SignUpState()
    object Success : SignUpState()
    data class Error(val message: String) : SignUpState()
}