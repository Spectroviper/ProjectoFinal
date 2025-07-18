package com.example.projecttest.challengeEditedCreated

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
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
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.projecttest.MyId
import com.example.projecttest.about.AboutActivity
import com.example.projecttest.challenge.ChallengeActivity
import com.example.projecttest.challenges.ChallengesActivity
import com.example.projecttest.friendList.FriendListActivity
import com.example.projecttest.game.GameActivity
import com.example.projecttest.gameEditedCreated.EditGameViewModel
import com.example.projecttest.gameEditedCreated.GameCreatedEditedScreen
import com.example.projecttest.games.GamesActivity
import com.example.projecttest.helpComposeFiles.popUps.LogoutConfirmationDialog
import com.example.projecttest.library.LibraryActivity
import com.example.projecttest.signIn.SignInActivity
import com.example.projecttest.ui.theme.YourAppTheme
import com.example.projecttest.user.UserActivity
import com.example.projecttest.users.UsersActivity
import java.time.Instant
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter


class EditChallengeActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            YourAppTheme {
                val viewModel = viewModel<EditChallengeViewModel>()
                val context = LocalContext.current
                val lifecycleOwner = context as LifecycleOwner
                val challengeId = intent.getStringExtra("challengeId")
                val user = MyId.user

                if (user == null || challengeId == null) {
                    LaunchedEffect(Unit) { finish() }
                    return@YourAppTheme
                }

                val challenge by viewModel.getChallengeByChallengeId(challengeId).observeAsState()

                if (challenge == null) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                } else {
                    challenge.let { existingChallenge ->
                        var challengeName by remember { mutableStateOf(existingChallenge?.challengeName ?: "") }
                        var imageUrl by remember { mutableStateOf(existingChallenge?.imageUrl ?: "") }
                        var type by remember { mutableStateOf(existingChallenge?.type ?: "") }
                        var selectedTab by remember { mutableIntStateOf(0) }
                        var option by remember { mutableStateOf("") }
                        var showPopup by remember { mutableStateOf(false) }

                        ChallengeEditedCreatedScreen(
                            challengeName = challengeName,
                            onChallengeNameChange = { challengeName = it },
                            type = type,
                            imageUrl = imageUrl,
                            onImageUrlChange = { imageUrl = it },
                            selectedTabIndex = 2,
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
                                    val intent = Intent(this, AboutActivity::class.java)
                                    startActivity(intent)
                                }
                                if (option == "LogOut") {
                                    showPopup = true
                                }
                            },
                            onFinishClick = {

                                if (imageUrl.isNotBlank() && imageUrl.startsWith("content://")) {
                                    viewModel.uploadChallengeImage(imageUrl.toUri(), context)
                                        .observe(lifecycleOwner) { uploadedUrl ->
                                            uploadedUrl?.let {
                                                viewModel.updateChallenge(
                                                    challengeId,
                                                    challengeName,
                                                    type,
                                                    it
                                                ).observe(lifecycleOwner) { result ->
                                                    if (result.successfull == true) {
                                                        context.startActivity(
                                                            Intent(
                                                                context,
                                                                ChallengeActivity::class.java
                                                            ).apply {
                                                                putExtra("challengeId", challengeId)
                                                            })
                                                    }
                                                }
                                            }
                                        }
                                } else if (true) {
                                    viewModel.updateChallenge(
                                        challengeId,
                                        challengeName,
                                        type,
                                        imageUrl,
                                    ).observe(lifecycleOwner) { result ->
                                        if (result.successfull == true) {
                                            context.startActivity(
                                                Intent(
                                                    context,
                                                    ChallengeActivity::class.java
                                                ).apply {
                                                    putExtra("challengeId", challengeId)
                                                })
                                        }
                                    }
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
}