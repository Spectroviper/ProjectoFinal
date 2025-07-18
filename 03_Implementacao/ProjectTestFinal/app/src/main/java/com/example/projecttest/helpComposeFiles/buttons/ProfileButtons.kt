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

@Composable
fun ProfileButtons(onEdit: () -> Unit = {}) {

    Button(
        onClick = onEdit,
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7FB800)),
        modifier = Modifier
            .padding(horizontal = 32.dp)
            .fillMaxWidth()
    ) {
        Text("Edit Profile", color = Color.White)
    }
}