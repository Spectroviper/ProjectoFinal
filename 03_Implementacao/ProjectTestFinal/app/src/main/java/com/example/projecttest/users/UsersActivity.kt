package com.example.projecttest.users

import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.LaunchedEffect
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
import com.example.projecttest.data.model.Game
import com.example.projecttest.data.model.User
import com.example.projecttest.friendList.FriendListActivity
import com.example.projecttest.games.GamesActivity
import com.example.projecttest.helpComposeFiles.popUps.LogoutConfirmationDialog
import com.example.projecttest.library.LibraryActivity
import com.example.projecttest.signIn.SignInActivity
import com.example.projecttest.ui.theme.YourAppTheme
import com.example.projecttest.user.UserActivity
import com.example.projecttest.usersScores.UsersScoresActivity

class UsersActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewModel = ViewModelProvider(this)[UsersViewModel::class.java]

        setContent {
            YourAppTheme {
                val userProfile = MyId.user
                Log.d("UsersActivity", "$userProfile")
                val users by viewModel.getAllUsers(userProfile?.userId.toString()).observeAsState(initial = emptyList())
                var selectedTab by remember { mutableIntStateOf(0) }
                var selectedMiddleTabIndex by remember { mutableIntStateOf(0) }
                var query by remember { mutableStateOf("") }
                var option by remember { mutableStateOf("") }
                var filteredUsers by remember { mutableStateOf<List<User>>(emptyList()) }
                var showPopup by remember { mutableStateOf(false) }
                LaunchedEffect(users) {
                    filteredUsers = users
                }
                UsersScreen(
                    users = filteredUsers,
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
                    query = query,
                    onQueryChange = { query = it },
                    onSearchClick = { viewModel.searchUsers(query).observe(this@UsersActivity)
                                        {filteredUsers = it}

                                    },
                    selectedMiddleTabIndex = 0,
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
                            putExtra("userId", user.userId)
                            putExtra("myUser", MyId.user)
                            if (user.userId != MyId.user?.userId)
                                putExtra("before", "users")
                            else
                                putExtra("before", "profile")
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