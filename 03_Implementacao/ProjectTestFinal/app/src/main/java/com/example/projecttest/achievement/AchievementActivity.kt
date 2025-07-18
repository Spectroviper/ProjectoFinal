package com.example.projecttest.achievement

import android.content.Intent
import android.os.Bundle
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
import com.example.projecttest.achievementEditedCreated.EditAchievementActivity
import com.example.projecttest.challenges.ChallengesActivity
import com.example.projecttest.friendList.FriendListActivity
import com.example.projecttest.game.GameActivity
import com.example.projecttest.games.GamesActivity
import com.example.projecttest.helpComposeFiles.popUps.LogoutConfirmationDialog
import com.example.projecttest.library.LibraryActivity
import com.example.projecttest.signIn.SignInActivity
import com.example.projecttest.ui.theme.YourAppTheme
import com.example.projecttest.user.UserActivity
import com.example.projecttest.users.UsersActivity

class AchievementActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewModel = ViewModelProvider(this)[AchievementViewModel::class.java]

        setContent {
            YourAppTheme {
                val user = MyId.user
                val gameId = intent.getStringExtra("gameId")
                val achievementId = intent.getStringExtra("achievementId")
                val achievement by viewModel.getAchievementByAchievementId(achievementId!!).observeAsState()
                var selectedTab by remember { mutableIntStateOf(0) }
                var option by remember { mutableStateOf("") }
                var showPopup by remember { mutableStateOf(false) }
                achievement?.let {
                    AchievementScreen(
                        achievement = it,
                        user = user!!,
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
                        onDelete = {
                            viewModel.deleteAchievement(achievementId!!)
                            val intent = Intent(this, GameActivity::class.java).apply {
                                putExtra("gameId", gameId)
                            }

                            startActivity(intent)
                                   },


                        onEdit = { val intent = Intent(this, EditAchievementActivity::class.java).apply {
                            putExtra("achievementId", achievementId)
                            putExtra("gameId", achievement!!.gameId)
                        }
                            startActivity(intent) },

                        onAvatarClick = {
                            val intent = Intent(this, UserActivity::class.java).apply {
                                putExtra("userId", MyId.user?.userId)
                                putExtra("myUser", MyId.user)
                                putExtra("before", "profile")
                            }
                            startActivity(intent)
                            finish()
                        },

                        onOptionSelected = { option = it
                            if(option == "About"){
                                val intent = Intent(this, AboutActivity::class.java)
                                startActivity(intent)
                            }
                            if (option == "LogOut") {
                                showPopup = true
                            }
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