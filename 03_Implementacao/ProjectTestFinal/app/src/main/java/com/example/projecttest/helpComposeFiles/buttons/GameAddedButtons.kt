package com.example.projecttest.helpComposeFiles.buttons

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import com.example.projecttest.data.model.Game
import com.example.projecttest.data.model.User
import com.example.projecttest.data.model.UserGame

@Composable
fun GameAddedButtons(
    onRemove: () -> Unit = {},

) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Button(
            onClick = onRemove,
            colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
            modifier = Modifier
                .padding(horizontal = 32.dp)
                .fillMaxWidth()
        ) {
            Text("Remove Game", color = Color.White)
        }

    }

}