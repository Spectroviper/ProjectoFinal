package com.example.projecttest.signup

import android.app.Activity
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.projecttest.signIn.SignInActivity

@Composable
fun SignUpScreen(viewModel: SignUpViewModel) {
    val context = LocalContext.current
    val signUpState = viewModel.signUpState

    LaunchedEffect(signUpState) {
        when (signUpState) {
            is SignUpState.Success -> {
                Toast.makeText(context, "Sign Up Successful", Toast.LENGTH_SHORT).show()
                context.startActivity(Intent(context, SignInActivity::class.java))
                (context as? Activity)?.finish()
            }
            is SignUpState.Error -> {
                println("Not working")
                Toast.makeText(context, signUpState.message, Toast.LENGTH_SHORT).show()
            }
            else -> Unit
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(80.dp))

        TextField(
            value = viewModel.email,
            onValueChange = viewModel::onEmailChange,
            label = { Text("Email") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = viewModel.username,
            onValueChange = viewModel::onUsernameChange,
            label = { Text("Username") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = viewModel.password,
            onValueChange = viewModel::onPasswordChange,
            label = { Text("Password") },
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = viewModel.confirmPassword,
            onValueChange = viewModel::onConfirmPasswordChange,
            label = { Text("Confirm Password") },
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = { viewModel.signUp() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Sign Up", fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Already have an account? Sign In",
            color = Color.Blue,
            modifier = Modifier.clickable {
                context.startActivity(Intent(context, SignInActivity::class.java))
                (context as? Activity)?.finish()
            }
        )
    }
}