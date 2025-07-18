package com.example.projecttest.userEdited

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.net.toUri
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.projecttest.MyId
import com.example.projecttest.about.AboutActivity
import com.example.projecttest.challenges.ChallengesActivity
import com.example.projecttest.data.model.User
import com.example.projecttest.friendList.FriendListActivity
import com.example.projecttest.games.GamesActivity
import com.example.projecttest.helpComposeFiles.popUps.LogoutConfirmationDialog
import com.example.projecttest.library.LibraryActivity
import com.example.projecttest.signIn.SignInActivity
import com.example.projecttest.ui.theme.YourAppTheme
import com.example.projecttest.user.UserActivity
import com.example.projecttest.users.UsersActivity


class EditUserActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            YourAppTheme {
                val viewModel = viewModel<EditUserViewModel>()
                val context = LocalContext.current
                val myUser = intent.getParcelableExtra("myUser", User::class.java)

                if (myUser == null) {
                    LaunchedEffect(Unit) { finish() }
                    return@YourAppTheme
                }

                var userName by remember { mutableStateOf(myUser.userName ?: "") }
                var imageUrl by remember { mutableStateOf(myUser.imageUrl ?: "") }
                var biography by remember { mutableStateOf(myUser.biography ?: "") }
                var selectedTab by remember { mutableIntStateOf(2) }
                var option by remember { mutableStateOf("") }
                var showPopup by remember { mutableStateOf(false) }

                UserEditedScreen(
                    userName = userName,
                    onUserNameChange = { userName = it },
                    imageUrl = imageUrl,
                    onImageUrlChange = { imageUrl = it },
                    biography = biography,
                    onBiographyChange = { biography = it },
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
                            val intent = Intent(context, AboutActivity::class.java)
                            context.startActivity(intent)
                        }
                        if (option == "LogOut") {
                            showPopup = true
                        }
                    },
                    onFinishClick = {
                        val lifecycleOwner = context as LifecycleOwner

                        if (imageUrl.startsWith("content://")) {
                            viewModel.uploadUserImage(imageUrl.toUri(), context).observe(lifecycleOwner) { uploadedUrl ->
                                uploadedUrl?.let {
                                    viewModel.updateUser(myUser.userId.toString(), userName, biography, it).observe(lifecycleOwner) {
                                        if (it.successfull == true) {
                                            val intent = Intent(context, UserActivity::class.java).apply {
                                                putExtra("userId", myUser.userId)
                                                putExtra("myUser", myUser)
                                                putExtra("before", "profile")
                                            }
                                            context.startActivity(intent)
                                        }
                                    }
                                }
                            }
                        } else {
                            viewModel.updateUser(myUser.userId.toString(), userName, biography, imageUrl).observe(lifecycleOwner) {
                                if (it.successfull == true) {
                                    val intent = Intent(context, UserActivity::class.java).apply {
                                        putExtra("userId", myUser.userId)
                                        putExtra("myUser", myUser)
                                        putExtra("before", "profile")
                                    }
                                    context.startActivity(intent)
                                }
                            }
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