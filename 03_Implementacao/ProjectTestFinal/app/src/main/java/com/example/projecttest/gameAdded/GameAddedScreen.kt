package com.example.projecttest.gameAdded

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
import com.example.projecttest.data.model.Achievement
import com.example.projecttest.data.model.Game
import com.example.projecttest.data.model.User
import com.example.projecttest.data.model.UserAchievement
import com.example.projecttest.data.model.UserGame
import com.example.projecttest.helpComposeFiles.bottomNav.BottomNavBar
import com.example.projecttest.helpComposeFiles.buttons.GameAddedButtons
import com.example.projecttest.helpComposeFiles.buttons.GameButtons
import com.example.projecttest.helpComposeFiles.info.GameAddedInfo
import com.example.projecttest.helpComposeFiles.info.GameInfo
import com.example.projecttest.helpComposeFiles.itemCards.AchievementItem
import com.example.projecttest.helpComposeFiles.itemCards.UserAchievementItem
import com.example.projecttest.helpComposeFiles.topNav.TopBar

@Composable
fun GameAddedScreen(
    userGame: UserGame,
    userAchievements: List<UserAchievement>,
    selectedTabIndex: Int = 1,
    onTabSelected: (Int) -> Unit = {},
    onRemove: () -> Unit = {},
    onAvatarClick: () -> Unit = {},
    onOptionSelected: (String) -> Unit,
    onUserAchievementClick: (UserAchievement) -> Unit = {}

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

            GameAddedInfo(userGame)

            Spacer(Modifier.height(8.dp))

            GameAddedButtons(onRemove)

            Spacer(Modifier.height(16.dp))

            Text(
                text = "Achievements",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            LazyColumn(
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .height(300.dp)
            ) {
                items(userAchievements) { userAchievement ->
                    UserAchievementItem(
                        userAchievement = userAchievement,
                        onClick = { onUserAchievementClick(userAchievement) })
                }
            }
        }
    }
}