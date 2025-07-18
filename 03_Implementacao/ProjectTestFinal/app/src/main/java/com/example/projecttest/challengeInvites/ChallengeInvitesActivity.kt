package com.example.projecttest.challengeInvites

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
import com.example.projecttest.challenge.ChallengeActivity
import com.example.projecttest.challengeAchievements.ChallengeAchievementsActivity
import com.example.projecttest.challenges.ChallengesActivity
import com.example.projecttest.data.model.ChallengeInvite
import com.example.projecttest.friendList.FriendListActivity
import com.example.projecttest.games.GamesActivity
import com.example.projecttest.helpComposeFiles.popUps.LogoutConfirmationDialog
import com.example.projecttest.helpComposeFiles.popUps.challengePopUps.InviteChallengePopUpAcceptedChallenge
import com.example.projecttest.helpComposeFiles.popUps.challengePopUps.InviteChallengePopUpPendingChallenge
import com.example.projecttest.helpComposeFiles.popUps.challengePopUps.InviteChallengePopUpRejectedChallenge
import com.example.projecttest.library.LibraryActivity
import com.example.projecttest.signIn.SignInActivity
import com.example.projecttest.ui.theme.YourAppTheme
import com.example.projecttest.user.UserActivity
import com.example.projecttest.userChallengeAchievements.UserChallengeAchievementsActivity
import com.example.projecttest.users.UsersActivity


class ChallengeInvitesActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewModel = ViewModelProvider(this)[ChallengeInvitesViewModel::class.java]

        setContent {
            YourAppTheme {
                val user = MyId.user
                val challengeId = intent.getStringExtra("challengeId")
                val challenge by viewModel.getChallengeByChallengeId(challengeId.toString())
                    .observeAsState()
                val challengeInvites by viewModel.getAllChallengeInvitesByChallengeId(challengeId.toString())
                    .observeAsState(initial = emptyList())
                val userChallengesPasser = user?.userChallenges
                Log.d("ChallengeInvitesChallenge", "$challengeInvites")
                var selectedTab by remember { mutableIntStateOf(0) }
                var query by remember { mutableStateOf("") }
                var option by remember { mutableStateOf("") }
                var selectedMiddleTab by remember { mutableIntStateOf(0) }
                var showPopup by remember { mutableStateOf(false) }
                var showPopupLogin by remember { mutableStateOf(false) }
                var selectedUser by remember { mutableStateOf<ChallengeInvite?>(null) }

                challengeInvites?.let {
                    ChallengeInvitesScreen(
                        challengeInvites = it,
                        selectedTabIndex = 4,
                        onOptionSelected = {
                            option = it
                            if (option == "About") {
                                val intent = Intent(this, AboutActivity::class.java)
                                startActivity(intent)
                            }
                            if (option == "LogOut") {
                                showPopupLogin = true
                            }
                        },
                        selectedMiddleTabIndex = 2,
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
                                        ChallengeAchievementsActivity::class.java
                                    ).apply {
                                        putExtra("challengeId", challengeId)

                                    }
                                startActivity(intent)
                            }

                            if (selectedMiddleTab == 2) {
                                val intent =
                                    Intent(
                                        this,
                                        ChallengeInvitesActivity::class.java
                                    ).apply {
                                        putExtra("challengeId", challengeId)
                                    }
                                startActivity(intent)
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
                        onSearchClick = { /* handle create */ },
                        onAvatarClick = {
                            val intent = Intent(this, UserActivity::class.java).apply {
                                putExtra("userId", MyId.user?.userId)
                                putExtra("myUser", MyId.user)
                                putExtra("before", "profile")
                            }
                            startActivity(intent)
                            finish()
                        },
                        onInviteClick = { userPopUp ->
                            selectedUser = userPopUp
                            showPopup = true
                        }


                    )

                    if (showPopupLogin) {
                        LogoutConfirmationDialog(

                            onConfirm = {
                                val intent = Intent(this, SignInActivity::class.java)
                                startActivity(intent)
                                showPopupLogin = false
                                finish()
                            },
                            onDismiss = {

                                showPopupLogin = false
                            },
                        )

                    }
                }




                if (showPopup && selectedUser != null) {

                    if (selectedUser?.state == "Pending") {
                        InviteChallengePopUpPendingChallenge(
                            challengeInvite = selectedUser!!,
                            onConfirm = {
                                viewModel.acceptChallengeInvite(selectedUser?.challengeInviteId!!, selectedUser?.challenge?.createdBy!!)
                                showPopup = false
                            },
                            onDismiss = {
                                viewModel.rejectChallengeInvite(selectedUser?.challengeInviteId!!, selectedUser?.challenge?.createdBy!!)
                                showPopup = false
                            },

                            )
                    }
                    if (selectedUser?.state == "Accepted") {
                        InviteChallengePopUpAcceptedChallenge(
                            challengeInvite = selectedUser!!,
                            onConfirm = {
                                viewModel.deleteChallengeInvite(selectedUser?.challengeInviteId!!)
                                showPopup = false
                            },
                            onDismiss = {
                                showPopup = false
                            }
                        )
                    }

                    if (selectedUser?.state == "Rejected") {
                        InviteChallengePopUpRejectedChallenge(
                            challengeInvite = selectedUser!!,
                            onConfirm = {
                                viewModel.deleteChallengeInvite(selectedUser?.challengeInviteId!!)
                                showPopup = false
                            },
                            onDismiss = {
                                showPopup = false
                            }
                        )
                    }
                }
            }
        }
    }
}