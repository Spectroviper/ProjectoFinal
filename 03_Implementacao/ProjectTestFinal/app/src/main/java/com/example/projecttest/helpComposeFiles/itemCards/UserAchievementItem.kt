package com.example.projecttest.helpComposeFiles.itemCards

import androidx.compose.foundation.Image
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.projecttest.AppConfig
import com.example.projecttest.data.model.Achievement
import com.example.projecttest.data.model.UserAchievement

@Composable
fun UserAchievementItem(userAchievement: UserAchievement,onClick: () -> Unit) {

    val BASE_URL = AppConfig.DOMAIN + AppConfig.PORT

    Card(
        modifier = Modifier.padding(vertical = 4.dp).fillMaxWidth().clickable { onClick() },
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = rememberAsyncImagePainter(BASE_URL + userAchievement.achievement?.imageUrl),
                contentDescription = userAchievement.achievement?.achievementName,
                modifier = Modifier.size(48.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(text = userAchievement.achievement?.achievementName!!,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis)

                Row {
                    userAchievement.achievement?.about?.let { Text(it, style = MaterialTheme.typography.bodySmall) }
                    Spacer(modifier = Modifier.weight(1f)) // Pushes the next Text to the end

                    Text(userAchievement.clear.toString(), style = MaterialTheme.typography.bodySmall)
                }
            }
        }
    }
}