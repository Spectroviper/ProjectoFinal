package com.example.projecttest.gameEditedCreated

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.projecttest.MyId
import com.example.projecttest.about.AboutActivity
import com.example.projecttest.games.GamesActivity
import com.example.projecttest.ui.theme.YourAppTheme
import com.example.projecttest.user.UserActivity
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import androidx.core.net.toUri
import com.example.projecttest.challenges.ChallengesActivity
import com.example.projecttest.friendList.FriendListActivity
import com.example.projecttest.helpComposeFiles.popUps.LogoutConfirmationDialog
import com.example.projecttest.library.LibraryActivity
import com.example.projecttest.signIn.SignInActivity
import com.example.projecttest.users.UsersActivity


class CreateGameActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            YourAppTheme {
                val viewModel = viewModel<CreateGameViewModel>()
                val context = LocalContext.current
                val user = MyId.user

                if (user == null) {
                    LaunchedEffect(Unit) { finish() }
                    return@YourAppTheme
                }

                var gameName by remember { mutableStateOf("") }
                var imageUrl by remember { mutableStateOf("") }
                var console by remember { mutableStateOf("") }
                var developer by remember { mutableStateOf("") }
                var publisher by remember { mutableStateOf("") }
                var genre by remember { mutableStateOf("") }
                var releaseDate by remember { mutableStateOf("") }
                var about by remember { mutableStateOf("") }
                var createdBy by remember { mutableStateOf(user.userId.toString()) }
                var selectedTab by remember { mutableIntStateOf(0) }
                var option by remember { mutableStateOf("") }
                var showDatePicker by remember { mutableStateOf(false) }
                var showPopup by remember { mutableStateOf(false) }

                val datePickerState = rememberDatePickerState()

                if (showDatePicker) {
                    DatePickerDialog(
                        onDismissRequest = { showDatePicker = false },
                        confirmButton = {
                            TextButton(onClick = {
                                datePickerState.selectedDateMillis?.let { millis ->
                                    val date = Instant.ofEpochMilli(millis)
                                        .atZone(ZoneId.systemDefault())
                                        .toLocalDate()
                                    releaseDate = date.toString()
                                }
                                showDatePicker = false
                            }) {
                                Text("OK")
                            }
                        },
                        dismissButton = {
                            TextButton(onClick = { showDatePicker = false }) {
                                Text("Cancel")
                            }
                        }
                    ) {
                        DatePicker(state = datePickerState)
                    }
                }

                GameCreatedEditedScreen(
                    gameName = gameName,
                    onGameNameChange = { gameName = it },
                    imageUrl = imageUrl,
                    onImageUrlChange = { imageUrl = it },
                    console = console,
                    onConsoleChange = { console = it },
                    developer = developer,
                    onDeveloperChange = { developer = it },
                    publisher = publisher,
                    onPublisherChange = { publisher = it },
                    genre = genre,
                    onGenreChange = { genre = it },
                    releaseDate = releaseDate,
                    onReleaseDateChange = { releaseDate = it },
                    about = about,
                    onAboutChange = { about = it },
                    createdBy = createdBy,
                    onCreatedBy = { createdBy = it },
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
                        val currentTime = java.time.LocalTime.now()
                        val dateTimeString = "$releaseDate ${currentTime.format(java.time.format.DateTimeFormatter.ofPattern("HH:mm:ss"))}"

                        val lifecycleOwner = context as LifecycleOwner

                        if (imageUrl.startsWith("content://")) {
                            viewModel.uploadGameImage(imageUrl.toUri(), context).observe(lifecycleOwner) { uploadedUrl ->
                                uploadedUrl?.let {
                                    viewModel.createGame(
                                        gameName,
                                        about,
                                        console,
                                        developer,
                                        publisher,
                                        genre,
                                        createdBy,
                                        dateTimeString,
                                        it
                                    ).observe(lifecycleOwner) {
                                        if (it.successfull == true) {
                                            val intent = Intent(context, GamesActivity::class.java)
                                            context.startActivity(intent)
                                        }
                                    }
                                }
                            }
                        } else {
                            viewModel.createGame(
                                gameName,
                                about,
                                console,
                                developer,
                                publisher,
                                genre,
                                createdBy,
                                dateTimeString,
                                imageUrl
                            ).observe(lifecycleOwner) {
                                if (it.successfull == true) {
                                    val intent = Intent(context, GamesActivity::class.java)
                                    context.startActivity(intent)
                                }
                            }
                        }
                    },
                    onPickDateClick = { showDatePicker = true }
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