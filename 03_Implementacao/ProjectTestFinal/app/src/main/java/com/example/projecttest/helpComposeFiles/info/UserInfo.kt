package com.example.projecttest.helpComposeFiles.info

import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.snapping.SnapPosition
import androidx.compose.foundation.layout.Arrangement.Center
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.projecttest.AppConfig
import com.example.projecttest.data.model.Game
import com.example.projecttest.data.model.User
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun UserInfo(user: User) {

    val BASE_URL = AppConfig.DOMAIN + AppConfig.PORT

    Column (
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        Image(
            painter = rememberAsyncImagePainter(model = BASE_URL + user.imageUrl),
            contentDescription = user.userName,
            modifier = Modifier
                .size(140.dp)
                .clip(CircleShape)
                .align(Alignment.CenterHorizontally)

        )
        Spacer(Modifier.width(16.dp))
        Column (
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.CenterHorizontally),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val memberSinceFormatted = user.memberSince?.take(10)

            val lastLoginFormatted = user.lastLogin?.take(10)


            Text("Username: ${user.userName}")
            Text("Email: ${user.email}")
            Text("Total Points: ${user.totalPoints}")
            Text("Average Completion: ${user.averageCompletion}")
            Text("Member Since: ${memberSinceFormatted}")
            Text("Last Login: ${lastLoginFormatted}")
        }
    }

    Text(
        text = "Biography",
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
            text = user.biography.toString(),
            modifier = Modifier.padding(16.dp),
            style = MaterialTheme.typography.bodyMedium
        )
    }
}