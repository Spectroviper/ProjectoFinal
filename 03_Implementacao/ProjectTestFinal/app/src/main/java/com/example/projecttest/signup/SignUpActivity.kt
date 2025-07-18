package com.example.projecttest.signup

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
//import com.example.generated.CreateUserMutation
//import com.example.generated.SignInQuery
//import com.example.project.databinding.ActivitySignUpBinding

class SignUpActivity : ComponentActivity() { // 👈 using ComponentActivity for Compose
    private val viewModel: SignUpViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SignUpScreen(viewModel)
        }
    }
}