package com.example.projecttest.achievementEditedCreated

import android.content.Intent
import android.os.Bundle
import android.util.Log
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
import com.example.projecttest.MyId
import com.example.projecttest.about.AboutActivity
import com.example.projecttest.challenges.ChallengesActivity
import com.example.projecttest.friendList.FriendListActivity
import com.example.projecttest.games.GamesActivity
import com.example.projecttest.library.LibraryActivity
import com.example.projecttest.ui.theme.YourAppTheme
import com.example.projecttest.user.UserActivity
import com.example.projecttest.users.UsersActivity
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.projecttest.game.GameActivity
import com.example.projecttest.helpComposeFiles.popUps.LogoutConfirmationDialog
import com.example.projecttest.signIn.SignInActivity

class CreateAchievementActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            YourAppTheme {
                val viewModel = viewModel<CreateAchievementViewModel>()
                val context = LocalContext.current
                val user = MyId.user
                val gameId = intent.getStringExtra("gameId")

                // Log initial state and inputs
                Log.d("CreateAchievementActivity", "onCreate called")
                Log.d("CreateAchievementActivity", "gameId: $gameId, user: $user")

                if (user == null || gameId == null) {
                    Log.e("CreateAchievementActivity", "Missing required data — finishing activity")
                    LaunchedEffect(Unit) { finish() }
                    return@YourAppTheme
                }

                var achievementName by remember { mutableStateOf("") }
                var imageUrl by remember { mutableStateOf("") }
                var totalCollectable by remember { mutableStateOf(1) }
                var about by remember { mutableStateOf("") }
                var selectedTab by remember { mutableIntStateOf(0) }
                var option by remember { mutableStateOf("") }
                var showPopup by remember { mutableStateOf(false) }

                AchievementCreatedEditedScreen(
                    achievementName = achievementName,
                    onAchievementNameChange = {
                        Log.d("CreateAchievementActivity", "AchievementName changed: $it")
                        achievementName = it
                    },
                    imageUrl = imageUrl,
                    onImageUrlChange = {
                        Log.d("CreateAchievementActivity", "ImageUrl changed: $it")
                        imageUrl = it
                    },
                    totalCollectable = totalCollectable,
                    onTotalCollectableChange = {
                        Log.d("CreateAchievementActivity", "TotalCollectable changed: $it")
                        totalCollectable = it
                    },
                    about = about,
                    onAboutChange = {
                        Log.d("CreateAchievementActivity", "About changed: $it")
                        about = it
                    },
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
                        Log.d("CreateAchievementActivity", "Option selected: $option")
                        if (option == "About") {
                            context.startActivity(Intent(context, AboutActivity::class.java))
                        }
                        if (option == "LogOut") {
                            showPopup = true
                        }
                    },
                    onFinishClick = {
                        val lifecycleOwner = context as LifecycleOwner

                        Log.d("CreateAchievementActivity", "Finish clicked")
                        Log.d("CreateAchievementActivity", "Achievement name: $achievementName")
                        Log.d("CreateAchievementActivity", "About: $about")
                        Log.d("CreateAchievementActivity", "Total Collectable: $totalCollectable")
                        Log.d("CreateAchievementActivity", "Image URL: $imageUrl")

                        if (imageUrl.startsWith("content://")) {
                            Log.d("CreateAchievementActivity", "Uploading image...")
                            viewModel.uploadAchievementImage(imageUrl.toUri(), context).observe(lifecycleOwner) { uploadedUrl ->
                                Log.d("CreateAchievementActivity", "Uploaded image URL: $uploadedUrl")
                                uploadedUrl?.let {
                                    Log.d("CreateAchievementActivity", "Calling createAchievement with uploaded URL")
                                    viewModel.createAchievement(
                                        achievementName,
                                        about,
                                        totalCollectable,
                                        user.userId.toString(),
                                        it,
                                        gameId

                                    ).observe(lifecycleOwner) { message ->
                                        Log.d("CreateAchievementActivity", "createAchievement result: ${message.successfull}")
                                        if (message.successfull == true) {
                                            Log.d("CreateAchievementActivity", "Achievement created successfully - navigating to GameActivity")
                                            val intent = Intent(context, GameActivity::class.java).apply {
                                                putExtra("gameId", gameId)
                                            }
                                            context.startActivity(intent)
                                        }
                                    }
                                } ?: Log.e("CreateAchievementActivity", "Uploaded URL was null!")
                            }
                        } else {
                            Log.d("CreateAchievementActivity", "Creating achievement with direct image URL")
                            viewModel.createAchievement(
                                achievementName,
                                about,
                                totalCollectable,
                                user.userId.toString(),
                                imageUrl,
                                gameId
                            ).observe(lifecycleOwner) { message ->
                                Log.d("CreateAchievementActivity", "createAchievement result: ${message.successfull}")
                                if (message.successfull == true) {
                                    Log.d("CreateAchievementActivity", "Achievement created successfully - navigating to GameActivity")
                                    val intent = Intent(context, GameActivity::class.java).apply {
                                        putExtra("gameId", gameId)
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

