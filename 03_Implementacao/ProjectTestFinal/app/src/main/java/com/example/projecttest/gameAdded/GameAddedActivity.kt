package com.example.projecttest.gameAdded

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
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
import com.example.projecttest.achievementAdded.AchievementAddedActivity
import com.example.projecttest.challenges.ChallengesActivity
import com.example.projecttest.friendList.FriendListActivity
import com.example.projecttest.games.GamesActivity
import com.example.projecttest.helpComposeFiles.popUps.LogoutConfirmationDialog
import com.example.projecttest.library.LibraryActivity
import com.example.projecttest.signIn.SignInActivity
import com.example.projecttest.ui.theme.YourAppTheme
import com.example.projecttest.user.UserActivity
import com.example.projecttest.users.UsersActivity

class GameAddedActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewModel = ViewModelProvider(this)[GameAddedViewModel::class.java]

        setContent {
            YourAppTheme {
                val userGameId = intent.getStringExtra("userGameId")
                Log.d("GameAddedActivity", "$userGameId")
                val user = MyId.user
                Log.d("GameAddedActivity", "$user")
                val userGame by viewModel.getUserGameByUserGameId(userGameId.toString()).observeAsState()
                Log.d("GameAddedActivity", "$userGame")
                val userAchievements by viewModel.getUserUserAchievements(user?.userId!!).observeAsState(initial = emptyList())
                Log.d("GameAddedActivity", "$userAchievements")
                if (userGame == null) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                } else
                {

                    var selectedTab by remember { mutableIntStateOf(0) }
                    var option by remember { mutableStateOf("") }
                    var showPopup by remember { mutableStateOf(false) }
                    userGame?.let { it ->
                        GameAddedScreen(
                            userGame = it,
                            userAchievements = userAchievements!!,
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
                            onRemove = {
                                viewModel.removeGameFromUser(user?.userId!!, userGame!!.game?.gameId!!)
                                val intent = Intent(this, LibraryActivity::class.java)
                                startActivity(intent)
                                finish() },

                            onAvatarClick = {
                                val intent = Intent(this, UserActivity::class.java).apply {
                                    putExtra("userId", user?.userId)
                                    putExtra("myUser", user)
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
                                    finish()
                                }
                                if (option == "LogOut") {
                                    showPopup = true
                                }
                            },
                            onUserAchievementClick = { userAchievement ->
                                val intent = Intent(this, AchievementAddedActivity::class.java).apply {
                                    putExtra("userGameId", userGameId)
                                    putExtra("userAchievementId", userAchievement.userAchievementId)
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


    }
}