package com.example.projecttest.helpComposeFiles.itemCards


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.projecttest.data.model.ChallengeInvite
import com.example.projecttest.data.model.Friend
import com.example.projecttest.data.model.User

@Composable
fun ChallengeInviteRejectedSentItem(user: User, challengeInvite: ChallengeInvite, onClick: () -> Unit) {

    if (!challengeInvite.isRequest!!) {
        Card(
            modifier = Modifier.padding(vertical = 4.dp).fillMaxWidth().clickable { onClick() },
            elevation = CardDefaults.cardElevation(4.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFFFCDD2))
        ) {
            Text(text = "The invite sent to ${user.userName} was rejected",
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis)
        }
    }
    else {
        Card(
            modifier = Modifier.padding(vertical = 4.dp).fillMaxWidth().clickable { onClick() },
            elevation = CardDefaults.cardElevation(4.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFFFCDD2))
        ) {
            Text(text = "Later",//"The invite sent to ${challengeInvite.challenge.challengeName} was rejected",
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis)
        }
    }
}