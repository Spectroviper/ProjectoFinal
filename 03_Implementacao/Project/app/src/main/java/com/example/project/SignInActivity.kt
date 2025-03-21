package com.example.project

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.generated.SignInQuery
import com.example.project.databinding.ActivitySignInBinding
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class SignInActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignInBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)


        firebaseAuth = FirebaseAuth.getInstance()
        binding.textView.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }

        binding.button.setOnClickListener {
            val username = binding.usernameEt.text.toString()
            val pass = binding.passET.text.toString()

            if (username.isNotEmpty() && pass.isNotEmpty()) {

                firebaseAuth.signInWithEmailAndPassword(username, pass).addOnCompleteListener {
                    if (it.isSuccessful) {
                        val userEmail = firebaseAuth.currentUser?.email

                        if (userEmail != null) {
                            lifecycleScope.launch {
                                try {
                                    val deferredUser = async {
                                        MyApolloClient.instance.query(SignInQuery(userEmail)).execute()
                                    }
                                    val response = deferredUser.await()
                                    val user = response.data?.signIn
                                    SessionManager.setCurrentUser(user)
                                    val intent = Intent(this@SignInActivity, MainActivity::class.java)
                                    startActivity(intent)
                                    Log.d("API", "User Settled" + SessionManager.currentUser.toString())

                                } catch (e: Exception) {
                                    Log.e("API", "Error executing SignInGraphQL query", e)
                                }
                            }
                        }

                    } else {
                        Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()

                    }
                }
            } else {
                Toast.makeText(this, "Empty Fields Are not Allowed !!", Toast.LENGTH_SHORT).show()

            }
        }
    }

    /* override fun onStart() {
        super.onStart()

        if(firebaseAuth.currentUser != null){
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    } */
}