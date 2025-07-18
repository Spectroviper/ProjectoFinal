package com.example.projecttest.friendListInvites

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
import com.example.projecttest.challenges.ChallengesActivity
import com.example.projecttest.data.model.Friend
import com.example.projecttest.friendList.FriendListActivity
import com.example.projecttest.games.GamesActivity
import com.example.projecttest.helpComposeFiles.popUps.LogoutConfirmationDialog
import com.example.projecttest.helpComposeFiles.popUps.friendsPopUps.InvitePopUpPending
import com.example.projecttest.helpComposeFiles.popUps.friendsPopUps.InvitePopUpRejected
import com.example.projecttest.library.LibraryActivity
import com.example.projecttest.signIn.SignInActivity
import com.example.projecttest.ui.theme.YourAppTheme
import com.example.projecttest.user.UserActivity
import com.example.projecttest.users.UsersActivity


class FriendListInvitesActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewModel = ViewModelProvider(this)[FriendListInvitesViewModel::class.java]

        setContent {
            YourAppTheme {
                val user = MyId.user
                Log.d("FriendListInvites", "$user")
                val friends by viewModel.getAllFriendInvites(user?.userId.toString())
                    .observeAsState(initial = emptyList())
                Log.d("FriendListInvites", "$friends")

                var selectedTab by remember { mutableIntStateOf(0) }
                var selectedMiddleTab by remember { mutableIntStateOf(0) }
                var query by remember { mutableStateOf("") }
                var option by remember { mutableStateOf("") }
                var showPopup by remember { mutableStateOf(false) }
                var showPopupLogin by remember { mutableStateOf(false) }
                var selectedUser by remember { mutableStateOf<Friend?>(null) }

                FriendListInvitesScreen(
                    friends = friends,
                    selectedTabIndex = 3,
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
                    selectedMiddleTabIndex = 1,
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
                    onInviteClick = { userInvite ->
                        selectedUser = userInvite
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





                if (showPopup && selectedUser != null) {

                    if (selectedUser!!.state == "Pending") {

                        InvitePopUpPending(
                            friend = selectedUser!!,
                            onConfirm = {
                                viewModel.acceptFriendInvite(selectedUser!!.friendId!!)
                                // Handle confirm logic here
                                showPopup = false
                            },
                            onDismiss = {
                                viewModel.rejectFriendInvite(selectedUser!!.friendId!!)
                                showPopup = false
                            }
                        )
                    }
                    if (selectedUser!!.state == "Rejected") {

                        InvitePopUpRejected(
                            friend = selectedUser!!,
                            onConfirm = {
                                // Handle confirm logic here
                                viewModel.deleteFriendInvite(selectedUser!!.friendId!!)
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