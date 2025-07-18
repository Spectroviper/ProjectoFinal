package com.example.projecttest.challenge

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue

import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModelProvider
import com.example.projecttest.MyId
import com.example.projecttest.about.AboutActivity
import com.example.projecttest.challengeAchievements.ChallengeAchievementsActivity
import com.example.projecttest.challengeEditedCreated.EditChallengeActivity
import com.example.projecttest.challenges.ChallengesActivity
import com.example.projecttest.data.model.Challenge
import com.example.projecttest.data.model.UserChallenge
import com.example.projecttest.friendList.FriendListActivity
import com.example.projecttest.games.GamesActivity
import com.example.projecttest.library.LibraryActivity
import com.example.projecttest.ui.theme.YourAppTheme
import com.example.projecttest.user.UserActivity
import com.example.projecttest.users.UsersActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.material3.CircularProgressIndicator
import com.example.projecttest.challengeInvites.ChallengeInvitesActivity
import com.example.projecttest.challengeNotUsers.ChallengeNotUsersActivity
import com.example.projecttest.helpComposeFiles.popUps.LogoutConfirmationDialog
import com.example.projecttest.signIn.SignInActivity
import com.example.projecttest.userChallengeAchievements.UserChallengeAchievementsActivity

class ChallengeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        val viewModel = ViewModelProvider(this)[ChallengeViewModel::class.java]


        setContent {
            YourAppTheme {

                val userProfile = MyId.user
                val user by viewModel.getUserByUserId(userProfile?.userId!!).observeAsState()
                //Log.d("ChallengeActivity", "$userProfile")
                val challengeId = intent.getStringExtra("challengeId")
                //Log.d("ChallengeActivity", challengeId.toString())
                val challenge by viewModel.getChallengeByChallengeId(challengeId.toString())
                    .observeAsState()

                if (challenge == null || user == null) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                } else {
                    Log.d("ChallengeActivity", "$challenge")
                    Log.d("ChallengeActivity", "${challenge!!.isStarted}")
                    val userChallenges = challenge?.userChallenges
                    val userChallengesPasser = user?.userChallenges
                    //Log.d("ChallengeActivity", "users: $userChallenges")
                    var selectedTab by remember { mutableIntStateOf(4) }
                    var selectedMiddleTab by remember { mutableIntStateOf(0) }
                    var option by remember { mutableStateOf("") }
                    var showPopup by remember { mutableStateOf(false) }

                    ChallengeScreen(
                        userChallenges = userChallenges!!,
                        challenge = challenge!!,
                        selectedTabIndex = selectedTab,
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
                        selectedTabMiddleIndex = selectedMiddleTab,
                        onTabMiddleSelected = {
                            selectedMiddleTab = it
                            if (selectedMiddleTab == 0) {
                                val intent =
                                    Intent(this, ChallengeActivity::class.java).apply {
                                        putExtra("challengeId", challengeId)
                                    }
                                startActivity(intent)
                            }
                            if (selectedMiddleTab == 1) {
                                if (challenge!!.isStarted == false) {
                                    val intent =
                                        Intent(
                                            this,
                                            ChallengeAchievementsActivity::class.java
                                        ).apply {
                                            putExtra("challengeId", challengeId)

                                        }
                                    startActivity(intent)
                                }
                                else
                                {
                                    val intent =
                                        Intent(
                                            this,
                                            UserChallengeAchievementsActivity::class.java
                                        ).apply {
                                            putExtra("challengeId", challengeId)
                                            putExtra("userChallengeId", userChallengesPasser?.find { it.challenge?.challengeId == challengeId }?.userChallengeId)
                                        }
                                    startActivity(intent)
                                }
                            }
                            if (selectedMiddleTab == 2) {
                                if (userProfile?.userId == challenge!!.createdBy && challenge!!.isStarted == false) {
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
                        onEnd = { viewModel.endChallenge(challengeId!!) },
                        onEliminate = {
                            viewModel.deleteChallenge(challengeId!!)
                            val intent = Intent(this, UserActivity::class.java).apply {
                                putExtra("userId", userProfile?.userId)
                                putExtra("myUser", userProfile)
                                putExtra("before", "profile")
                            }
                            startActivity(intent)
                        },
                        onStart = {
                            viewModel.startChallenge(
                                userProfile?.userId!!,
                                challengeId!!
                            )
                            Log.d("ChallengeActivity", "Start Date: " + challenge!!.startDate.toString())
                            Log.d("ChallengeActivity", "Start Challenge: " + challenge!!.isStarted)
                            val intent = Intent(this, ChallengeActivity::class.java).apply {
                                putExtra("challengeId", challengeId)
                            }
                            startActivity(intent)
                        },
                        onAdd = {
                            val intent = Intent(this, ChallengeNotUsersActivity::class.java).apply {
                                putExtra("challengeId", challengeId)
                            }
                            startActivity(intent)
                        },
                        onEdit = {
                            val intent = Intent(this, EditChallengeActivity::class.java).apply {
                                putExtra("challengeId", challengeId)
                            }
                            startActivity(intent)
                        },
                        onInvite = {
                            viewModel.inviteUserToChallenge(
                                userProfile?.userId!!,
                                challengeId!!,
                                true
                            )
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
                        onUserClick = { userChallenge ->
                            if (userChallenge.user?.userId == userProfile?.userId) {
                                val intent = Intent(this, UserActivity::class.java).apply {
                                    putExtra("userId", userChallenge.user?.userId)
                                    putExtra("myUser", userChallenge.user)
                                    putExtra("before", "profile")
                                }
                                startActivity(intent)
                            } else {
                                val intent = Intent(this, UserActivity::class.java).apply {
                                    putExtra("userId", userChallenge.user?.userId)
                                    putExtra("before", "games")
                                }
                                startActivity(intent)
                            }
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

