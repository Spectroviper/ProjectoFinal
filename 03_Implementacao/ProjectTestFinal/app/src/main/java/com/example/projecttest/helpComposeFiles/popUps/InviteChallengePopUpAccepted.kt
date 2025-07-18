package com.example.projecttest.helpComposeFiles.popUps

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.projecttest.data.model.Challenge
import com.example.projecttest.data.model.ChallengeInvite
import com.example.projecttest.data.model.User

@Composable
fun InviteChallengePopUpAccepted(
    user: User,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {

    AlertDialog(
        onDismissRequest = onDismiss,

        title = {
            Text(
                "The ${user.userName} was accepted.",
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(8.dp))
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7FB800))
            ) { Text("Confirm", color = Color.White) }
        },
    )


}