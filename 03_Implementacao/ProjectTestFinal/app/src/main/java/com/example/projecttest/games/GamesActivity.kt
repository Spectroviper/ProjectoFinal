package com.example.projecttest.games

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
import com.example.projecttest.data.model.ChallengeInvite
import com.example.projecttest.data.model.Game
import com.example.projecttest.friendList.FriendListActivity
import com.example.projecttest.game.GameActivity
import com.example.projecttest.gameEditedCreated.CreateGameActivity
import com.example.projecttest.helpComposeFiles.popUps.LogoutConfirmationDialog
import com.example.projecttest.helpComposeFiles.popUps.challengePopUps.InviteChallengePopUpPendingChallenge
import com.example.projecttest.library.LibraryActivity
import com.example.projecttest.signIn.SignInActivity
import com.example.projecttest.ui.theme.YourAppTheme
import com.example.projecttest.user.UserActivity
import com.example.projecttest.users.UsersActivity

class GamesActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewModel = ViewModelProvider(this)[GamesViewModel::class.java]

        setContent {
            YourAppTheme {
                val games by viewModel.getAllGames().observeAsState(initial = emptyList())
                val user = MyId.user
                var selectedTab by remember { mutableIntStateOf(0) }
                var query by remember { mutableStateOf("") }
                var filter by remember { mutableStateOf("Name") }
                var option by remember { mutableStateOf("") }
                var filteredGames by remember { mutableStateOf<List<Game>>(emptyList()) }
                var showPopup by remember { mutableStateOf(false) }
                LaunchedEffect(games) {
                    filteredGames = games
                }

                GamesScreen(
                    games = filteredGames,
                    selectedTabIndex = 1,
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
                    selectedFilter = filter,
                    onFilterSelected = { filter = it },
                    onOptionSelected = {
                        option = it
                        if (option == "About") {
                            val intent = Intent(this, AboutActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                        if (option == "LogOut") {
                            showPopup = true
                        }
                    },
                    onSearchClick = {

                        if (filter == "Name") {
                            viewModel.searchGames(gameName = query).observe(this@GamesActivity) {
                                filteredGames = it
                            }
                        }

                        if (filter == "Console") {
                            viewModel.searchGames(console = query).observe(this@GamesActivity) {
                                filteredGames = it
                            }
                        }

                        if (filter == "Developer") {
                            viewModel.searchGames(console = query).observe(this@GamesActivity) {
                                filteredGames = it
                            }
                        }

                        if (filter == "Publisher") {
                            viewModel.searchGames(console = query).observe(this@GamesActivity) {
                                filteredGames = it
                            }
                        }

                        if (filter == "Genre") {
                            viewModel.searchGames(console = query).observe(this@GamesActivity) {
                                filteredGames = it
                            }
                        }
                    },
                    onCreateGameClick = {
                        val intent = Intent(this, CreateGameActivity::class.java)
                        startActivity(intent)
                        finish()
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
                    onGameClick = { game ->
                        val gameId = game.gameId
                        val intent = Intent(this, GameActivity::class.java).apply {
                            putExtra("gameId", gameId)
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