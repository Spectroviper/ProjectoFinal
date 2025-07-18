package com.example.projecttest.helpComposeFiles.buttons

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun GamesButtons(onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier.padding(16.dp).fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6EBF44))
    ) {
        Text("Create Game")
    }
}