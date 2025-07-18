package com.example.projecttest.user

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState

import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import com.example.projecttest.MyId
import com.example.projecttest.about.AboutActivity
import com.example.projecttest.challenge.ChallengeActivity
import com.example.projecttest.challenges.ChallengesActivity
import com.example.projecttest.data.model.ChallengeInvite
import com.example.projecttest.data.model.Friend
import com.example.projecttest.data.model.User
import com.example.projecttest.friendList.FriendListActivity
import com.example.projecttest.games.GamesActivity
import com.example.projecttest.helpComposeFiles.popUps.LogoutConfirmationDialog
import com.example.projecttest.library.LibraryActivity
import com.example.projecttest.signIn.SignInActivity
import com.example.projecttest.ui.theme.YourAppTheme
import com.example.projecttest.userEdited.EditUserActivity
import com.example.projecttest.users.UsersActivity

class UserActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewModel = ViewModelProvider(this)[UserViewModel::class.java]
        val userId = intent.getStringExtra("userId")
        Log.d("User", userId.toString())
        val myUser = intent.getParcelableExtra("myUser", User::class.java)
        Log.d("User", myUser.toString())
        val before = intent.getStringExtra("before")
        Log.d("User", before.toString())

        setContent {
            YourAppTheme {
                val userProfile = MyId.user
                val user by viewModel.getUserByUserId(userId.toString()).observeAsState()
                Log.d("User", user.toString())
                if (user == null) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                } else {
                    val challenges = user?.userChallenges?.map { it.challenge!! }!!
                    Log.d("User", "$challenges")
                    var selectedTab by remember { mutableIntStateOf(0) }
                    var option by remember { mutableStateOf("") }
                    var showPopup by remember { mutableStateOf(false) }
                    user?.let { it ->
                        UserScreen(
                            user = it,
                            challenges = challenges,
                            before = before.toString(),
                            selectedTabIndex = 2,
                            onTabSelected = {
                                selectedTab = it
                                if (selectedTab == 0) {
                                    val intent = Intent(this, LibraryActivity::class.java)
                                    startActivity(intent)
                                    finish()
                                }
                                if (selectedTab == 1) {
                                    val intent = Intent(this, GamesActivity::class.java)
                                    startActivity(intent)
                                    finish()
                                }
                                if (selectedTab == 2) {
                                    val intent = Intent(this, UsersActivity::class.java)
                                    startActivity(intent)
                                    finish()
                                }
                                if (selectedTab == 3) {
                                    val intent = Intent(this, FriendListActivity::class.java)
                                    startActivity(intent)
                                    finish()
                                }
                                if (selectedTab == 4) {
                                    val intent = Intent(this, ChallengesActivity::class.java)
                                    startActivity(intent)
                                    finish()
                                }
                            },
                            onInvite = { viewModel.invite(userProfile?.userId!!,user!!.userId!!) },
                            onEdit = {
                                val intent = Intent(this, EditUserActivity::class.java).apply {
                                    putExtra("myUser", myUser)
                                }
                                startActivity(intent)
                            },
                            onAvatarClick = {
                                val intent = Intent(this, UserActivity::class.java).apply {
                                    putExtra("userId", MyId.user?.userId)
                                    putExtra("myUser", MyId.user)
                                    putExtra("before", "profile")
                                }
                                startActivity(intent)
                                finish()
                            },
                            onOptionSelected = {
                                option = it
                                if (option == "About") {
                                    val intent = Intent(this, AboutActivity::class.java)
                                    startActivity(intent)
                                }
                                if (option == "LogOut") {
                                    showPopup = true
                                }
                            },
                            onChallengeClick = { challenge ->
                                val intent = Intent(this, ChallengeActivity::class.java).apply {
                                    putExtra("challengeId", challenge.challengeId)
                                }
                                startActivity(intent)
                            },
                        )

                        if (showPopup) {
                            LogoutConfirmationDialog(

                                onConfirm = {
                                    val intent = Intent(this, SignInActivity::class.java)
                                    startActivity(intent)
                                    showPopup = false
                                    finish()
                                },
                                onDismiss = {

                                    showPopup = false
                                },
                            )

                        }

                    }
                }

            }
        }
    }
}