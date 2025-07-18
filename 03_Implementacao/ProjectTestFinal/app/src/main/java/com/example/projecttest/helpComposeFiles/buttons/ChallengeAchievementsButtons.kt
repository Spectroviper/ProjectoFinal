package com.example.projecttest.helpComposeFiles.buttons

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.projecttest.MyId
import com.example.projecttest.data.model.Challenge
import com.example.projecttest.data.model.User

@Composable
fun ChallengeAchievementsButtons (challenge: Challenge,
                                 onAdd: () -> Unit = {},) {

    if (MyId.user?.userId == challenge.createdBy && challenge.isStarted == false) {
        Button(
            onClick = onAdd,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7FB800)),
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text("Add Achievement", color = Color.White)
        }
    }
}