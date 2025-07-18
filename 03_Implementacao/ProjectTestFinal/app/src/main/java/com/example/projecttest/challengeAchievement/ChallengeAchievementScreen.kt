package com.example.projecttest.challengeAchievement

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.projecttest.data.model.Achievement
import com.example.projecttest.data.model.Challenge
import com.example.projecttest.data.model.ChallengeAchievement
import com.example.projecttest.data.model.User
import com.example.projecttest.helpComposeFiles.bottomNav.BottomNavBar
import com.example.projecttest.helpComposeFiles.buttons.AchievementButtons
import com.example.projecttest.helpComposeFiles.buttons.ChallengeAchievementButtons
import com.example.projecttest.helpComposeFiles.info.AchievementInfo
import com.example.projecttest.helpComposeFiles.info.ChallengeAchievementInfo
import com.example.projecttest.helpComposeFiles.topNav.TopBar

@Composable
fun ChallengeAchievementScreen(
    challengeAchievement: ChallengeAchievement,
    challenge: Challenge,
    user: User,
    selectedTabIndex: Int = 1,
    onTabSelected: (Int) -> Unit = {},
    onRemove: () -> Unit = {},
    onAvatarClick: () -> Unit = {},
    onOptionSelected: (String) -> Unit,

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
                .background(Color(0xFFE6ECFF)) // light blue

        ) {
            TopBar(onAvatarClick, onOptionSelected)

            Spacer(Modifier.height(8.dp))

            ChallengeAchievementInfo(challengeAchievement)

            Spacer(Modifier.height(8.dp))

            ChallengeAchievementButtons(user,challenge,onRemove)
        }
    }
}