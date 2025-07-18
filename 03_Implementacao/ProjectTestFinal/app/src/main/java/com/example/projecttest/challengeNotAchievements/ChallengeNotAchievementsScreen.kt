package com.example.projecttest.challengeNotAchievements

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
import com.example.projecttest.data.model.Challenge
import com.example.projecttest.helpComposeFiles.bottomNav.BottomNavBar
import com.example.projecttest.helpComposeFiles.info.ChallengeInfo
import com.example.projecttest.helpComposeFiles.itemCards.AchievementItem
import com.example.projecttest.helpComposeFiles.searchBars.ChallengesSearchBar
import com.example.projecttest.helpComposeFiles.searchBars.SearchBar
import com.example.projecttest.helpComposeFiles.topNav.TopBar


@Composable
fun ChallengeNotAchievementsScreen(
    achievements: List<Achievement>,
    selectedTabIndex: Int = 1,
    onTabSelected: (Int) -> Unit = {},
    query: String,
    onQueryChange: (String) -> Unit = {},
    selectedFilter: String,
    onFilterSelected: (String) -> Unit,
    onSearchClick: () -> Unit = {},
    onAvatarClick: () -> Unit = {},
    onOptionSelected: (String) -> Unit,
    onAchievementClick: (Achievement) -> Unit = {}

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

            ChallengesSearchBar(query, onQueryChange, selectedFilter, onFilterSelected, onSearchClick)

            Spacer(Modifier.height(8.dp))

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
                items(achievements) { achievement ->
                    AchievementItem(
                        achievement = achievement,
                        onClick = { onAchievementClick(achievement) })
                }
            }
        }
    }
}