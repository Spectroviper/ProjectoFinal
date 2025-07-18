package com.example.projecttest.achievementEditedCreated

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.net.toUri
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.projecttest.MyId
import com.example.projecttest.about.AboutActivity
import com.example.projecttest.achievement.AchievementActivity
import com.example.projecttest.challenges.ChallengesActivity
import com.example.projecttest.friendList.FriendListActivity
import com.example.projecttest.games.GamesActivity
import com.example.projecttest.helpComposeFiles.popUps.LogoutConfirmationDialog
import com.example.projecttest.library.LibraryActivity
import com.example.projecttest.signIn.SignInActivity
import com.example.projecttest.ui.theme.YourAppTheme
import com.example.projecttest.user.UserActivity
import com.example.projecttest.users.UsersActivity

class EditAchievementActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            YourAppTheme {
                val viewModel = viewModel<EditAchievementViewModel>()
                val context = LocalContext.current
                val lifecycleOwner = context as LifecycleOwner
                val user = MyId.user

                if (user == null) {
                    LaunchedEffect(Unit) { finish() }
                    return@YourAppTheme
                }

                val achievementId = intent.getStringExtra("achievementId")
                val gameId = intent.getStringExtra("gameId")


                if (achievementId == null || gameId == null) {
                    LaunchedEffect(Unit) { finish() }
                    return@YourAppTheme
                }

                val achievement by viewModel.getAchievementByAchievementId(achievementId).observeAsState()

                if (achievement == null) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                } else {
                    achievement?.let { existingAchievement ->
                        var achievementName by remember { mutableStateOf(existingAchievement.achievementName ?: "") }
                        var imageUrl by remember { mutableStateOf(existingAchievement.imageUrl ?: "") }
                        var totalCollectable by remember { mutableStateOf(existingAchievement.totalCollectable) }
                        var about by remember { mutableStateOf(existingAchievement.about ?: "") }
                        var selectedTab by remember { mutableIntStateOf(0) }
                        var option by remember { mutableStateOf("") }
                        var showPopup by remember { mutableStateOf(false) }

                        AchievementCreatedEditedScreen(
                            achievementName = achievementName,
                            onAchievementNameChange = { achievementName = it },
                            imageUrl = imageUrl,
                            onImageUrlChange = { imageUrl = it },
                            totalCollectable = totalCollectable,
                            onTotalCollectableChange = { totalCollectable = it },
                            about = about,
                            onAboutChange = { about = it },
                            selectedTabIndex = selectedTab,
                            onTabSelected = { selectedTab = it
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
                                if (imageUrl.startsWith("content://")) {
                                    viewModel.uploadAchievementImage(imageUrl.toUri(), context)
                                        .observe(lifecycleOwner) { uploadedUrl ->
                                            uploadedUrl?.let {
                                                viewModel.updateAchievement(
                                                    achievementId,
                                                    achievementName,
                                                    about,
                                                    totalCollectable!!,
                                                    it
                                                ).observe(lifecycleOwner) { result ->
                                                    if (result.successfull == true) {
                                                        context.startActivity(
                                                            Intent(context, AchievementActivity::class.java).apply {
                                                                putExtra("achievementId", achievementId)
                                                                putExtra("gameId", gameId)
                                                            }
                                                        )
                                                    }
                                                }
                                            }
                                        }
                                } else {
                                    viewModel.updateAchievement(
                                        achievementId,
                                        achievementName,
                                        about,
                                        totalCollectable!!,
                                        imageUrl
                                    ).observe(lifecycleOwner) { result ->
                                        if (result.successfull == true) {
                                            context.startActivity(
                                                Intent(context, AchievementActivity::class.java).apply {
                                                    putExtra("achievementId", achievementId)
                                                    putExtra("gameId", gameId)
                                                }
                                            )
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
                    } ?: LaunchedEffect(Unit) {
                        finish()
                    }
                }
            }
        }
    }
}