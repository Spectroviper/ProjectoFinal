package com.example.projecttest.user

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.projecttest.data.model.Challenge
import com.example.projecttest.data.model.ChallengeInvite
import com.example.projecttest.data.model.Friend
import com.example.projecttest.data.model.User
import com.example.projecttest.helpComposeFiles.bottomNav.BottomNavBar
import com.example.projecttest.helpComposeFiles.buttons.UserButtons
import com.example.projecttest.helpComposeFiles.info.UserInfo
import com.example.projecttest.helpComposeFiles.itemCards.ChallengeListItem
import com.example.projecttest.helpComposeFiles.topNav.TopBar

@Composable
fun UserScreen(
    user: User,
    challenges: List<Challenge?>,
    before: String,
    selectedTabIndex: Int = 1,
    onTabSelected: (Int) -> Unit = {},
    onInvite: () -> Unit = {},
    onEdit: () -> Unit = {},
    onAvatarClick: () -> Unit = {},
    onOptionSelected: (String) -> Unit,
    onChallengeClick: (Challenge) -> Unit,
    ) {
    Scaffold(
        bottomBar = {
            BottomNavBar(
                selectedIndex = selectedTabIndex,
                onTabSelected = onTabSelected
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(paddingValues)
                .background(Color(0xFFE6ECFF))

        ) {
            TopBar(onAvatarClick, onOptionSelected)

            Spacer(Modifier.height(8.dp))

            UserInfo(user)

            Spacer(Modifier.height(8.dp))

            UserButtons(before,onInvite,onEdit)

            Spacer(Modifier.height(16.dp))

            Text(
                text = "User Challenges",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            LazyColumn(
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .height(300.dp)
            ) {
                items(challenges) { challenge ->
                    ChallengeListItem(
                        challenge = challenge!!,
                        onClick = { onChallengeClick(challenge) })
                }
            }
        }
    }
}