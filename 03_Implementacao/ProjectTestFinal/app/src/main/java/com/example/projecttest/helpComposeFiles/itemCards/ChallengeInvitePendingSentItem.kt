package com.example.projecttest.helpComposeFiles.itemCards


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
import com.example.projecttest.data.model.User

@Composable
fun ChallengeInvitePendingSentItem(user: User, challengeInvite: ChallengeInvite) {

    if (!challengeInvite.isRequest!!) {
        Card(
            modifier = Modifier.padding(vertical = 4.dp).fillMaxWidth(),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Text(text = "The invite sent to ${user.userName} is pending",
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis)
        }
    }
    else {
        Card(
            modifier = Modifier.padding(vertical = 4.dp).fillMaxWidth(),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Text(text = "Later",//"The invite sent to ${challengeInvite.challenge.challengeName} is pending",
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis)
        }
    }

}