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
import com.example.projecttest.data.model.UserAchievement

@Composable
fun AchievementAddedButtons(
    userAchievement: UserAchievement,
    onFinish: () -> Unit = {},
    onRestart: () -> Unit = {},
) {


    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        if (userAchievement.clear == false) {

            Button(
                onClick = onFinish,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7FB800))
            ) { Text("Finish Achievement", color = Color.White) }
        }
        else {
            Button(
                onClick = onRestart,
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
            ) { Text("Restart Achievement", color = Color.White) }
        }
    }
}