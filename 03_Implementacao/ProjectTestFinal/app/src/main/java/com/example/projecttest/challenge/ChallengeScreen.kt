package com.example.projecttest.challenge

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.projecttest.MyId
import com.example.projecttest.data.model.Challenge
import com.example.projecttest.data.model.User
import com.example.projecttest.data.model.UserChallenge
import com.example.projecttest.helpComposeFiles.bottomNav.BottomNavBar
import com.example.projecttest.helpComposeFiles.buttons.ChallengeButtons
import com.example.projecttest.helpComposeFiles.info.ChallengeInfo
import com.example.projecttest.helpComposeFiles.itemCards.UserChallengeListItem
import com.example.projecttest.helpComposeFiles.itemCards.UserListItem
import com.example.projecttest.helpComposeFiles.middleNav.MiddleChallengeNavBar
import com.example.projecttest.helpComposeFiles.topNav.TopBar

@Composable
fun ChallengeScreen(
    userChallenges: List<UserChallenge>,
    challenge: Challenge,
    selectedTabIndex: Int = 1,
    onTabSelected: (Int) -> Unit = {},
    selectedTabMiddleIndex: Int = 1,
    onTabMiddleSelected: (Int) -> Unit = {},
    onEnd: () -> Unit = {},
    onEliminate: () -> Unit = {},
    onStart: () -> Unit = {},
    onAdd: () -> Unit = {},
    onEdit: () -> Unit = {},
    onInvite: () -> Unit = {},
    onAvatarClick: () -> Unit = {},
    onOptionSelected: (String) -> Unit,
    onUserClick: (UserChallenge) -> Unit = {}

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

            MiddleChallengeNavBar(selectedTabMiddleIndex,onTabMiddleSelected)

            Spacer(Modifier.height(8.dp))

            ChallengeInfo(challenge)

            Spacer(Modifier.height(8.dp))



            ChallengeButtons(userChallenges,challenge,onStart,onAdd,onEdit,onEnd,onEliminate,onInvite)

            Text(
                text = "Users",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            LazyColumn(
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .height(300.dp)
            ) {

                items(userChallenges) { userChallenge ->
                    UserChallengeListItem(
                        userChallenge = userChallenge,
                        onClick = { onUserClick(userChallenge) })
                }
            }
        }
    }
}