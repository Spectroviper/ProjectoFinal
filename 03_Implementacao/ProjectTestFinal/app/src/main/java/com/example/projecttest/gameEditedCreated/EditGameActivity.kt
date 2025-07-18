package com.example.projecttest.gameEditedCreated

import android.content.Intent
import android.os.Bundle
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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.projecttest.MyId
import com.example.projecttest.about.AboutActivity
import com.example.projecttest.challenges.ChallengesActivity
import com.example.projecttest.friendList.FriendListActivity
import com.example.projecttest.game.GameActivity
import com.example.projecttest.games.GamesActivity
import com.example.projecttest.helpComposeFiles.popUps.LogoutConfirmationDialog
import com.example.projecttest.library.LibraryActivity
import com.example.projecttest.signIn.SignInActivity
import com.example.projecttest.ui.theme.YourAppTheme
import com.example.projecttest.user.UserActivity
import com.example.projecttest.users.UsersActivity
import java.time.Instant
import java.time.ZoneId


class EditGameActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            YourAppTheme {
                val viewModel = viewModel<EditGameViewModel>()
                val context = LocalContext.current
                val lifecycleOwner = context as LifecycleOwner
                val user = MyId.user
                val gameId = intent.getStringExtra("gameId")

                if (user == null || gameId == null) {
                    LaunchedEffect(Unit) { finish() }
                    return@YourAppTheme
                }

                val game by viewModel.getGameByGameId(gameId).observeAsState()

                if (game == null) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                } else {
                    game?.let { existingGame ->
                        var gameName by remember { mutableStateOf(existingGame.gameName ?: "") }
                        var imageUrl by remember { mutableStateOf(existingGame.imageUrl ?: "") }
                        var console by remember { mutableStateOf(existingGame.console ?: "") }
                        var developer by remember { mutableStateOf(existingGame.developer ?: "") }
                        var publisher by remember { mutableStateOf(existingGame.publisher ?: "") }
                        var genre by remember { mutableStateOf(existingGame.genre ?: "") }
                        var releaseDate by remember {
                            mutableStateOf(
                                existingGame.releaseDate ?: ""
                            )
                        }
                        var about by remember { mutableStateOf(existingGame.about ?: "") }
                        var createdBy by remember { mutableStateOf(existingGame.createdBy ?: "") }
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
                            createdBy = user.userId.toString(),
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
                                val intent = Intent(context, UserActivity::class.java).apply {
                                    putExtra("userId", user.userId)
                                    putExtra("myUser", user)
                                    putExtra("before", "profile")
                                }
                                context.startActivity(intent)
                            },
                            onOptionSelected = {
                                option = it
                                if (option == "About") {
                                    context.startActivity(
                                        Intent(
                                            context,
                                            AboutActivity::class.java
                                        )
                                    )
                                }
                                if (option == "LogOut") {
                                    showPopup = true
                                }
                            },
                            onFinishClick = {
                                val currentTime = java.time.LocalTime.now()
                                val dateTimeString = "$releaseDate ${
                                    currentTime.format(
                                        java.time.format.DateTimeFormatter.ofPattern("HH:mm:ss")
                                    )
                                }"

                                if (imageUrl.isNotBlank() && imageUrl.startsWith("content://")) {
                                    viewModel.uploadGameImage(imageUrl.toUri(), context)
                                        .observe(lifecycleOwner) { uploadedUrl ->
                                            uploadedUrl?.let {
                                                viewModel.updateGame(
                                                    gameId,
                                                    gameName,
                                                    about,
                                                    console,
                                                    developer,
                                                    publisher,
                                                    genre,
                                                    dateTimeString,
                                                    it
                                                ).observe(lifecycleOwner) { result ->
                                                    if (result.successfull == true) {
                                                        context.startActivity(
                                                            Intent(
                                                                context,
                                                                GameActivity::class.java
                                                            ).apply {
                                                                putExtra("gameId", gameId)
                                                            })
                                                    }
                                                }
                                            }
                                        }
                                } else if (true) {
                                    viewModel.updateGame(
                                        gameId,
                                        gameName,
                                        about,
                                        console,
                                        developer,
                                        publisher,
                                        genre,
                                        dateTimeString,
                                        imageUrl
                                    ).observe(lifecycleOwner) { result ->
                                        if (result.successfull == true) {
                                            context.startActivity(
                                                Intent(
                                                    context,
                                                    GameActivity::class.java
                                                ).apply {
                                                    putExtra("gameId", gameId)
                                                })
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


                    } ?: LaunchedEffect(Unit) {
                        finish()
                    }
                }
            }
        }
    }
}