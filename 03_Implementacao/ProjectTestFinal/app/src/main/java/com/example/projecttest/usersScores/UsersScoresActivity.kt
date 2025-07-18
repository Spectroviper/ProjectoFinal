package com.example.projecttest.usersScores

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModelProvider
import com.example.projecttest.MyId
import com.example.projecttest.about.AboutActivity
import com.example.projecttest.challenges.ChallengesActivity
import com.example.projecttest.friendList.FriendListActivity
import com.example.projecttest.games.GamesActivity
import com.example.projecttest.helpComposeFiles.popUps.LogoutConfirmationDialog
import com.example.projecttest.library.LibraryActivity
import com.example.projecttest.signIn.SignInActivity
import com.example.projecttest.ui.theme.YourAppTheme
import com.example.projecttest.user.UserActivity
import com.example.projecttest.users.UsersActivity

class UsersScoresActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewModel = ViewModelProvider(this)[UsersScoresViewModel::class.java]
        val userProfile = MyId.user

        if (userProfile == null) {
            Log.e("UsersScoresActivity", "Missing required intent extras or user")
            finish() // or show a Toast and return
            return
        }

        setContent {
            YourAppTheme {
                val users by viewModel.getAllUsersByTotalPoints().observeAsState(initial = emptyList())
                var selectedTab by remember { mutableIntStateOf(0) }
                var selectedMiddleTabIndex by remember { mutableIntStateOf(0) }
                var option by remember { mutableStateOf("") }
                var showPopup by remember { mutableStateOf(false) }
                UsersScreen(
                    user = userProfile,
                    users = users,
                    selectedTabIndex = 2,
                    onOptionSelected = { option = it
                        if(option == "About"){
                            val intent = Intent(this, AboutActivity::class.java)
                            startActivity(intent)
                        }
                        if (option == "LogOut") {
                            showPopup = true
                        }
                    },
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
                    selectedMiddleTabIndex = 1,
                    onMiddleTabSelected = {
                        selectedMiddleTabIndex = it
                        if (selectedMiddleTabIndex == 0) {
                            val intent =
                                Intent(this, UsersActivity::class.java)
                            startActivity(intent)
                        }
                        if (selectedMiddleTabIndex == 1) {
                            val intent =
                                Intent(this, UsersScoresActivity::class.java)
                            startActivity(intent)
                        }
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
                    onUserClick = { user ->
                        val intent = Intent(this, UserActivity::class.java).apply {
                            if (user.userId == MyId.user?.userId){
                                putExtra("userId", user.userId)
                                putExtra("myUser", MyId.user)
                                putExtra("before", "profile")
                            }
                            else{
                                putExtra("userId", user.userId)
                                putExtra("myUser", MyId.user)
                                putExtra("before", "users")
                            }
                        }
                        startActivity(intent)
                    }
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