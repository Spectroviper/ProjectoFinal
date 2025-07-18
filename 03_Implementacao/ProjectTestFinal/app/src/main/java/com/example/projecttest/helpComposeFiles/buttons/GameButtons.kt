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
fun GameButtons(
    user: User,
    game: Game,
    onAdd: () -> Unit = {},
    onDelete: () -> Unit = {},
    onEdit: () -> Unit = {},
    onCreateAchievement: () -> Unit = {}
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {


        if (user.userId == game.createdBy) {
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.SpaceEvenly
            )
            {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(
                        onClick = onAdd,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7FB800))
                    ) { Text("Add Game", color = Color.White) }

                    Button(
                        onClick = onDelete,
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                    ) { Text("Delete Game", color = Color.White) }

                    Button(
                        onClick = onEdit,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7FB800))
                    ) { Text("Edit Game", color = Color.White) }


                }
                Spacer(Modifier.height(8.dp))
                Button(
                    onClick = onCreateAchievement,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7FB800)),
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Text("Create Achievement", color = Color.White)
                }
            }
        } else {
            Button(
                onClick = onAdd,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7FB800)),
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text("Add Game", color = Color.White)
            }
        }


    }

}