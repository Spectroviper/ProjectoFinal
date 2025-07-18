package com.example.projecttest.helpComposeFiles.buttons

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.projecttest.data.model.Achievement
import com.example.projecttest.data.model.Game
import com.example.projecttest.data.model.User

@Composable
fun AchievementButtons(
    user: User,
    achievement: Achievement,
    onEdit: () -> Unit = {},
    onDelete: () -> Unit = {},
) {


    if (user.userId == achievement.createdBy) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = onEdit,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7FB800))
            ) { Text("Edit Achievement", color = Color.White) }

            Button(
                onClick = onDelete,
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
            ) { Text("Delete Achievement", color = Color.White) }
        }
    }
}