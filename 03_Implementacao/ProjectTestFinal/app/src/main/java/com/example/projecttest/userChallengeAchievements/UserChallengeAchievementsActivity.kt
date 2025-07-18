package com.example.projecttest.userChallengeAchievements

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
import com.example.projecttest.challenge.ChallengeActivity
import com.example.projecttest.challengeAchievement.ChallengeAchievementActivity
import com.example.projecttest.challengeInvites.ChallengeInvitesActivity
import com.example.projecttest.challengeNotAchievements.ChallengeNotAchievementsActivity
import com.example.projecttest.challenges.ChallengesActivity
import com.example.projecttest.friendList.FriendListActivity
import com.example.projecttest.games.GamesActivity
import com.example.projecttest.helpComposeFiles.popUps.LogoutConfirmationDialog
import com.example.projecttest.library.LibraryActivity
import com.example.projecttest.signIn.SignInActivity
import com.example.projecttest.ui.theme.YourAppTheme
import com.example.projecttest.user.UserActivity
import com.example.projecttest.userChallengeAchievement.UserChallengeAchievementActivity
import com.example.projecttest.users.UsersActivity

class UserChallengeAchievementsActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewModel = ViewModelProvider(this)[UserChallengeAchievementsViewModel::class.java]



        setContent {
            YourAppTheme {
                val userProfile = MyId.user
                val challengeId = intent.getStringExtra("challengeId")
                val challenge by viewModel.getChallengeByChallengeId(challengeId!!).observeAsState()
                val userChallengeId = intent.getStringExtra("userChallengeId")


                val userChallengeDetails by viewModel.getUserChallengeByUserChallengeId(
                    userChallengeId.toString()
                ).observeAsState()
                if (userChallengeDetails == null || challenge == null) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                } else {
                    Log.d("UserChallengeAchievement", "$userChallengeDetails")
                    val userChallengeAchievements =
                        userChallengeDetails?.userChallengeAchievements
                    Log.d("UserChallengeAchievement", "$userChallengeAchievements")
                    var query by remember { mutableStateOf("") }
                    var selectedTab by remember { mutableIntStateOf(0) }
                    var selectedMiddleTab by remember { mutableIntStateOf(0) }
                    var option by remember { mutableStateOf("") }
                    var showPopup by remember { mutableStateOf(false) }


                    challenge?.let { it ->
                        UserChallengeAchievementsScreen(
                            challenge = challenge!!,
                            userChallengeAchievements = userChallengeAchievements!!,
                            selectedTabIndex = 4,
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
                                    val intent =
                                        Intent(this, FriendListActivity::class.java)
                                    startActivity(intent)
                                    finish()
                                }
                                if (selectedTab == 4) {
                                    val intent =
                                        Intent(this, ChallengesActivity::class.java)
                                    startActivity(intent)
                                    finish()
                                }
                            },
                            selectedMiddleTabIndex = 1,
                            onMiddleTabSelected = {
                                selectedMiddleTab = it
                                if (selectedMiddleTab == 0) {
                                    val intent =
                                        Intent(this, ChallengeActivity::class.java).apply {
                                            putExtra("challengeId", challengeId)
                                        }
                                    startActivity(intent)
                                }
                                if (selectedMiddleTab == 1) {
                                    val intent =
                                        Intent(
                                            this,
                                            UserChallengeAchievementsActivity::class.java
                                        ).apply {
                                            putExtra("challengeId", challengeId)
                                        }
                                    startActivity(intent)
                                }

                                if (selectedMiddleTab == 2) {
                                    if (userProfile?.userId == challenge!!.createdBy) {
                                        val intent =
                                            Intent(
                                                this,
                                                ChallengeInvitesActivity::class.java
                                            ).apply {
                                                putExtra("challengeId", challengeId)
                                            }
                                        startActivity(intent)
                                    }
                                }
                            },
                            query = query,
                            onQueryChange = { query = it },
                            onSearchClick = { /* handle create */ },
                            onAdd = {
                                val intent =
                                    Intent(
                                        this,
                                        ChallengeNotAchievementsActivity::class.java
                                    ).apply {
                                        putExtra("challengeId", challengeId)
                                    }
                                startActivity(intent)
                            },
                            onAvatarClick = {
                                val intent = Intent(this, UserActivity::class.java).apply {
                                    putExtra("userId", userProfile?.userId)
                                    putExtra("myUser", userProfile)
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
                            onAchievementClick = { userChallengeAchievement ->

                                val intent = Intent(
                                    this,
                                    UserChallengeAchievementActivity::class.java
                                ).apply {
                                    putExtra("challengeId", challengeId)
                                    putExtra(
                                        "userChallengeAchievementId",
                                        userChallengeAchievement.userChallengeAchievementId
                                    )
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
    }
}
