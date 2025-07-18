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
import com.example.projecttest.data.model.Game
import com.example.projecttest.data.model.User

@Composable
fun UserListItem(user: User, onClick: () -> Unit) {

    val BASE_URL = AppConfig.DOMAIN + AppConfig.PORT

    Card(
        modifier = Modifier.padding(vertical = 4.dp).fillMaxWidth().clickable { onClick() },
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = rememberAsyncImagePainter(BASE_URL + user.imageUrl),
                contentDescription = user.userName,
                modifier = Modifier.size(48.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(text = user.userName.toString(),
                     fontWeight = FontWeight.Bold,
                     maxLines = 1,
                     overflow = TextOverflow.Ellipsis)
                Text(user.totalPoints.toString(), style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}