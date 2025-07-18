package com.example.projecttest.helpComposeFiles.itemCards.challengeInvitesUser


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.projecttest.data.model.ChallengeInvite

@Composable
fun ChallengeInvitePendingReceivedUserItem(challengeInvite: ChallengeInvite, onClick: () -> Unit) {

    Card(
        modifier = Modifier
            .padding(vertical = 4.dp)
            .fillMaxWidth()
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Text(
            text = "The challenge ${challengeInvite.challenge?.challengeName} is requesting you to join the fun",
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }

}