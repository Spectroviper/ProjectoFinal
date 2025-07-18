package com.example.projecttest.signIn

import android.app.Activity
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Yellow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.projecttest.R
import com.example.projecttest.games.GamesActivity
import com.example.projecttest.library.LibraryActivity
import com.example.projecttest.signup.SignUpActivity

@Composable
fun SignInScreen(viewModel: SignInViewModel) {
    val signInState by viewModel.signInState.observeAsState()

    // State holders for input fields
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val context = LocalContext.current


    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.signin_screen),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .align(Alignment.Center),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Type your Email") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = Yellow,
                        unfocusedBorderColor = Yellow,
                        focusedLabelColor = Yellow,
                        cursorColor = Yellow
                ),
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Email
            )
            )

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Type your Password") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Yellow,
                    unfocusedBorderColor = Yellow,
                    focusedLabelColor = Yellow,
                    cursorColor = Yellow
                ),
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Password
                )
            )

            Button(
                onClick = {
                    viewModel.signIn(email, password)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Yellow,
                    contentColor = Color.Black
                )
            ) {
                Text(
                    text = "Sign In",
                    fontWeight = FontWeight.Bold
                )
            }


            Text(
                text = "Not Registered Yet, Sign Up!",
                color = Color.Black,
                modifier = Modifier
                    .clickable {
                        // Navigate to SignUp
                        Log.d("SignIn", "Sign in button clicked")
                        context.startActivity(Intent(context, SignUpActivity::class.java))
                    }
                    .padding(top = 16.dp)
            )
        }
    }

    // Handle login success/failure
    LaunchedEffect(signInState) {
        when (signInState) {
            is SignInState.Success -> {
                context.startActivity(Intent(context, GamesActivity::class.java))
                (context as? Activity)?.finish()
            }
            is SignInState.Error -> {
                Toast.makeText(
                    context,
                    (signInState as SignInState.Error).message,
                    Toast.LENGTH_SHORT
                ).show()
            }
            else -> Unit
        }
    }
}