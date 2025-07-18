package com.example.projecttest.helpComposeFiles.itemCards.challengeInvitesUser


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.projecttest.data.model.ChallengeInvite

@Composable
fun ChallengeInviteAcceptedUserItem(challengeInvite: ChallengeInvite, onClick: () -> Unit) {
    Card(
        modifier = Modifier.padding(vertical = 4.dp).fillMaxWidth().clickable { onClick() },
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFCDD2))
    ) {
        Text(
            text = "The invite sent to ${challengeInvite.challenge?.challengeName} was accepted",
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}