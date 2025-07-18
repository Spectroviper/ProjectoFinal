package com.example.projecttest.challengeInvitesUser

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import com.example.projecttest.MyId
import com.example.projecttest.data.model.ChallengeInvite
import com.example.projecttest.helpComposeFiles.bottomNav.BottomNavBar
import com.example.projecttest.helpComposeFiles.itemCards.challengeInvitesChallenge.ChallengeInviteAcceptedChallengeItem
import com.example.projecttest.helpComposeFiles.itemCards.challengeInvitesChallenge.ChallengeInvitePendingReceivedChallengeItem
import com.example.projecttest.helpComposeFiles.itemCards.challengeInvitesChallenge.ChallengeInvitePendingSentChallengeItem
import com.example.projecttest.helpComposeFiles.itemCards.challengeInvitesChallenge.ChallengeInviteRejectedChallengeItem
import com.example.projecttest.helpComposeFiles.itemCards.challengeInvitesUser.ChallengeInviteAcceptedUserItem
import com.example.projecttest.helpComposeFiles.itemCards.challengeInvitesUser.ChallengeInvitePendingReceivedUserItem
import com.example.projecttest.helpComposeFiles.itemCards.challengeInvitesUser.ChallengeInvitePendingSentUserItem
import com.example.projecttest.helpComposeFiles.itemCards.challengeInvitesUser.ChallengeInviteRejectedUserItem
import com.example.projecttest.helpComposeFiles.middleNav.MiddleChallengesNavBar
import com.example.projecttest.helpComposeFiles.searchBars.SearchBar
import com.example.projecttest.helpComposeFiles.topNav.TopBar


@Composable
fun ChallengeInvitesUserScreen(
    challengeInvites : List<ChallengeInvite>,
    selectedTabIndex: Int = 1,
    onTabSelected: (Int) -> Unit = {},
    query: String,
    onQueryChange: (String) -> Unit = {},
    onSearchClick: () -> Unit = {},
    onAvatarClick: () -> Unit = {},
    onOptionSelected: (String) -> Unit,
    selectedMiddleTabIndex: Int = 1,
    onMiddleTabSelected: (Int) -> Unit = {},
    onInviteClick: (ChallengeInvite) -> Unit = {}
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
                .background(Color(0xFFEAF0FF))
        ) {
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = 64.dp)
                ) {

                    TopBar(onAvatarClick, onOptionSelected)
                    MiddleChallengesNavBar(selectedMiddleTabIndex,onMiddleTabSelected)
                    SearchBar(query, onQueryChange, onSearchClick)
                    Text(
                        text = "Challenge Invites",
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )

                    LazyColumn(
                        modifier = Modifier
                            .padding(horizontal = 8.dp)
                            .height(600.dp)
                    ) {

                        items(challengeInvites) { challengeInvite ->

                            if (challengeInvite.user?.userId == MyId.user?.userId && challengeInvite.state == "Pending" && challengeInvite.isRequest == false)
                                ChallengeInvitePendingReceivedUserItem(challengeInvite, onClick = {onInviteClick(challengeInvite) })

                            if (challengeInvite.user?.userId == MyId.user?.userId && challengeInvite.state == "Pending" && challengeInvite.isRequest == true)
                                ChallengeInvitePendingSentUserItem(challengeInvite)

                            if (challengeInvite.user?.userId == MyId.user?.userId && challengeInvite.state == "Accepted" && challengeInvite.isRequest == true)
                                ChallengeInviteAcceptedUserItem(challengeInvite, onClick = {onInviteClick(challengeInvite) })

                            if (challengeInvite.user?.userId == MyId.user?.userId && challengeInvite.state == "Rejected" && challengeInvite.isRequest == true)
                                ChallengeInviteRejectedUserItem(challengeInvite, onClick = {onInviteClick(challengeInvite) })

                        }
                    }
                }
            }
        }
    }
}