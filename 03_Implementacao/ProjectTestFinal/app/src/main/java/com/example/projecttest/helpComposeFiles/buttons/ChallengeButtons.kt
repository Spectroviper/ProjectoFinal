package com.example.projecttest.helpComposeFiles.buttons

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.projecttest.MyId
import com.example.projecttest.data.model.Challenge
import com.example.projecttest.data.model.User
import com.example.projecttest.data.model.UserChallenge

@Composable
fun ChallengeButtons(
    userChallenges: List<UserChallenge?>,
    challenge: Challenge?,
    onStart: () -> Unit = {},
    onAdd: () -> Unit = {},
    onEdit: () -> Unit = {},
    onEnd: () -> Unit = {},
    onEliminate: () -> Unit = {},
    onInvite: () -> Unit = {}
) {


    if (userChallenges.any { it?.user?.userId == MyId.user?.userId && it?.user?.userId == challenge?.createdBy } && challenge?.isStarted == false) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {

            Button(
                onClick = onAdd,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7FB800))
            ) {
                Text("Add User", color = Color.White)
            }

            Button(
                onClick = onEdit,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7FB800))
            ) {
                Text("Edit Challenge", color = Color.White)
            }

            Button(
                onClick = onEliminate,
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
            ) {
                Text("Eliminate Challenge", color = Color.White)
            }


        }
        Button(
            onClick = onStart,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7FB800))
        ) { Text("Start Challenge", color = Color.White) }
        Spacer(Modifier.height(16.dp))
    }

    if (userChallenges.any { it?.user?.userId == MyId.user?.userId && it?.user?.userId == challenge?.createdBy } && challenge?.isStarted == true)
    {
        Button(
            onClick = onEnd,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
        ) { Text("End Challenge", color = Color.White) }
        Spacer(Modifier.height(16.dp))

        Button(
            onClick = onEliminate,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
        ) { Text("Destroy Challenge", color = Color.White) }
        Spacer(Modifier.height(16.dp))
    }

    if (userChallenges.none { it?.user?.userId == MyId.user?.userId } && challenge?.isStarted == false) {
        Button(
            onClick = onInvite,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7FB800))
        ) { Text("Enter Group", color = Color.White) }
        Spacer(Modifier.height(16.dp))
    }
}
