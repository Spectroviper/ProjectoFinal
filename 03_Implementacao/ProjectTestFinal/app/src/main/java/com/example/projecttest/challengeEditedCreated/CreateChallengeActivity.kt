package com.example.projecttest.challengeEditedCreated

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
import com.example.projecttest.friendList.FriendListActivity
import com.example.projecttest.games.GamesActivity
import com.example.projecttest.helpComposeFiles.popUps.LogoutConfirmationDialog
import com.example.projecttest.library.LibraryActivity
import com.example.projecttest.signIn.SignInActivity
import com.example.projecttest.ui.theme.YourAppTheme
import com.example.projecttest.user.UserActivity
import com.example.projecttest.users.UsersActivity


class CreateChallengeActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            YourAppTheme {
                val viewModel = viewModel<CreateChallengeViewModel>()
                val context = LocalContext.current
                val user = MyId.user

                if (user == null) {
                    LaunchedEffect(Unit) { finish() }
                    return@YourAppTheme
                }

                var challengeName by remember { mutableStateOf("") }
                var imageUrl by remember { mutableStateOf("") }
                var type by remember { mutableStateOf("") }
                var selectedTab by remember { mutableIntStateOf(0) }
                var option by remember { mutableStateOf("") }
                var showPopup by remember { mutableStateOf(false) }

                ChallengeEditedCreatedScreen(
                    challengeName = challengeName,
                    onChallengeNameChange = { challengeName = it },
                    imageUrl = imageUrl,
                    onImageUrlChange = { imageUrl = it },
                    type = "endfirst",
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
                            context.startActivity(Intent(context, AboutActivity::class.java))
                        }
                        if (option == "LogOut") {
                            showPopup = true
                        }
                    },
                    onFinishClick = {
                        val lifecycleOwner = context as LifecycleOwner

                        if (imageUrl.startsWith("content://")) {
                            viewModel.uploadChallengeImage(imageUrl.toUri(), context).observe(lifecycleOwner) { uploadedUrl ->
                                uploadedUrl?.let {
                                    viewModel.createChallenge(
                                        user.userId.toString(),
                                        challengeName,
                                        type,
                                        it
                                    ).observe(lifecycleOwner) { message ->
                                        if (message.successfull == true) {
                                            context.startActivity(Intent(context, ChallengesActivity::class.java))
                                        }
                                    }
                                }
                            }
                        } else {
                            viewModel.createChallenge(
                                user.userId.toString(),
                                challengeName,
                                type,
                                imageUrl
                            ).observe(lifecycleOwner) { message ->
                                if (message.successfull == true) {
                                    context.startActivity(Intent(context, ChallengesActivity::class.java))
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
