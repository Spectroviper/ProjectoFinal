package com.example.projecttest.library

import android.content.Intent
import android.os.Bundle
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
import com.example.projecttest.data.model.UserGame
import com.example.projecttest.friendList.FriendListActivity
import com.example.projecttest.gameAdded.GameAddedActivity
import com.example.projecttest.games.GamesActivity
import com.example.projecttest.helpComposeFiles.popUps.LogoutConfirmationDialog
import com.example.projecttest.signIn.SignInActivity
import com.example.projecttest.ui.theme.YourAppTheme
import com.example.projecttest.user.UserActivity
import com.example.projecttest.users.UsersActivity

class LibraryActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewModel = ViewModelProvider(this)[LibraryViewModel::class.java]

        setContent {
            YourAppTheme {
                val user = MyId.user
                Log.d("LibraryActivity", "$user")
                val userGames by viewModel.getUserUserGames(user?.userId!!).observeAsState(initial = emptyList())
                Log.d("LibraryActivity", "$userGames")
                var selectedTab by remember { mutableIntStateOf(0) }
                var query by remember { mutableStateOf("") }
                var option by remember { mutableStateOf("") }
                var filteredGames by remember { mutableStateOf<List<UserGame>>(emptyList()) }
                var showPopup by remember { mutableStateOf(false) }
                LaunchedEffect(userGames) {
                    filteredGames = userGames
                }
                LibraryScreen(
                    userGames = filteredGames,
                    selectedTabIndex = 0,
                    onOptionSelected = { option = it
                        if(option == "About"){
                            val intent = Intent(this, AboutActivity::class.java)
                            startActivity(intent)
                            finish()
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
                    onSearchClick = {

                        viewModel.searchUserGames(user?.userId!!,gameName = query).observe(this@LibraryActivity) {

                            filteredGames = it
                            Log.d("LibraryActivity", "Fieltered Games$filteredGames")
                    } },
                    onAvatarClick = {
                        val intent = Intent(this, UserActivity::class.java).apply {
                            putExtra("userId", MyId.user?.userId)
                            putExtra("myUser", MyId.user)
                            putExtra("before", "profile")
                        }
                        startActivity(intent)
                        finish()},
                    onUserGameClick = { userGame ->
                        val intent = Intent(this, GameAddedActivity::class.java).apply {
                            putExtra("userGameId", userGame.userGameId)
                        }
                        startActivity(intent)
                        finish()
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