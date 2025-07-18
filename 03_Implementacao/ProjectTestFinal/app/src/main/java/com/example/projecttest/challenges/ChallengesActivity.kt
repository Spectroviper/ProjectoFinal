package com.example.projecttest.challenges

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
import com.example.projecttest.challenge.ChallengeActivity
import com.example.projecttest.challengeEditedCreated.CreateChallengeActivity
import com.example.projecttest.challengeInvitesUser.ChallengeInvitesUserActivity
import com.example.projecttest.friendList.FriendListActivity
import com.example.projecttest.games.GamesActivity
import com.example.projecttest.helpComposeFiles.popUps.LogoutConfirmationDialog
import com.example.projecttest.library.LibraryActivity
import com.example.projecttest.signIn.SignInActivity
import com.example.projecttest.ui.theme.YourAppTheme
import com.example.projecttest.user.UserActivity
import com.example.projecttest.users.UsersActivity

class ChallengesActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewModel = ViewModelProvider(this)[ChallengesViewModel::class.java]


        setContent {
            YourAppTheme {
                val user = MyId.user
                val challenges by viewModel.getAllJoinableChallenges(MyId.user?.userId!!).observeAsState(initial = emptyList())
                var selectedTab by remember { mutableIntStateOf(0) }
                var selectedMiddleTab by remember { mutableIntStateOf(0) }
                var query by remember { mutableStateOf("") }
                var option by remember { mutableStateOf("") }
                var showPopup by remember { mutableStateOf(false) }

                ChallengesScreen(
                    challenges = challenges!!,
                    selectedTabIndex = 4,
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
                    selectedMiddleTabIndex = 0,
                    onMiddleTabSelected = {
                        selectedMiddleTab = it
                        if (selectedMiddleTab == 0) {
                            val intent =
                                Intent(this, ChallengesActivity::class.java)
                            startActivity(intent)
                        }
                        if (selectedMiddleTab == 1) {
                            val intent =
                                Intent(this, ChallengeInvitesUserActivity::class.java)
                            startActivity(intent)
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
                    onChallengeClick = { challenge ->
                        val intent = Intent(this, ChallengeActivity::class.java).apply {
                            putExtra("challengeId", challenge.challengeId)
                        }
                        startActivity(intent)
                    },
                    onCreateChallengeClick = {
                        val intent = Intent(this, CreateChallengeActivity::class.java)
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