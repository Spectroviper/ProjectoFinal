package com.example.projecttest.helpComposeFiles.info

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.projecttest.AppConfig
import com.example.projecttest.data.model.Achievement
import com.example.projecttest.data.model.Game

@Composable
fun AchievementInfo(achievement: Achievement) {
    val BASE_URL = AppConfig.DOMAIN + AppConfig.PORT

    Column (
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        Image(
            painter = rememberAsyncImagePainter(model = BASE_URL + achievement.imageUrl),
            contentDescription = achievement.achievementName,
            modifier = Modifier
                .size(140.dp)
                .clip(CircleShape)
                .align(Alignment.CenterHorizontally)

        )
        Spacer(Modifier.width(16.dp))
        Text(achievement.achievementName!!,

            modifier = Modifier
                .padding(32.dp)
                .align(Alignment.CenterHorizontally),

            fontSize = TextUnit(24F, TextUnitType.Sp),
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.width(16.dp))
        Column (
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.CenterHorizontally),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text("Progress: 0/${achievement.totalCollectable}")
        }
    }

    Text(
        text = "About",
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp,
        modifier = Modifier.padding(horizontal = 16.dp)
    )

    Card(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Text(
            text = achievement.about!!,
            modifier = Modifier.padding(16.dp),
            style = MaterialTheme.typography.bodyMedium
        )
    }
}