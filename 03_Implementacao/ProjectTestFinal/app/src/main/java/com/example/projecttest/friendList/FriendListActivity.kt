package com.example.projecttest.friendList

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.LaunchedEffect
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
import com.example.projecttest.challenges.ChallengesActivity
import com.example.projecttest.data.model.Friend
import com.example.projecttest.data.model.User
import com.example.projecttest.data.model.UserGame
import com.example.projecttest.friendListInvites.FriendListInvitesActivity
import com.example.projecttest.games.GamesActivity
import com.example.projecttest.helpComposeFiles.popUps.LogoutConfirmationDialog
import com.example.projecttest.library.LibraryActivity
import com.example.projecttest.signIn.SignInActivity
import com.example.projecttest.ui.theme.YourAppTheme
import com.example.projecttest.user.UserActivity
import com.example.projecttest.users.UsersActivity

class FriendListActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewModel = ViewModelProvider(this)[FriendListViewModel::class.java]



        setContent {
            YourAppTheme {
                val user = MyId.user
                val users by viewModel.getAllUserFriends(user?.userId.toString())
                    .observeAsState(initial = emptyList())

                var selectedTab by remember { mutableIntStateOf(0) }
                var selectedMiddleTab by remember { mutableIntStateOf(0) }
                var query by remember { mutableStateOf("") }
                var option by remember { mutableStateOf("") }
                var filteredFriends by remember { mutableStateOf<List<User>>(emptyList()) }
                var showPopup by remember { mutableStateOf(false) }

                LaunchedEffect(users) {
                    filteredFriends = users
                }

                FriendListScreen(
                    users = filteredFriends,
                    selectedTabIndex = 3,
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
                    selectedMiddleTabIndex = 0,
                    onMiddleTabSelected = {
                        selectedMiddleTab = it
                        if (selectedMiddleTab == 0) {
                            val intent = Intent(this, FriendListActivity::class.java)
                            startActivity(intent)
                        }
                        if (selectedMiddleTab == 1) {
                            val intent = Intent(this, FriendListInvitesActivity::class.java)
                            startActivity(intent)
                        }
                    },
                    query = query,
                    onQueryChange = { query = it },
                    onSearchClick = {
                        viewModel.searchUsers(query).observe(this@FriendListActivity)
                        { filteredFriends = it }

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