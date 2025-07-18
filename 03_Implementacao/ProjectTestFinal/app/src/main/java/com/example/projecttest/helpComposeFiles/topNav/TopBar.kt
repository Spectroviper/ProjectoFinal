package com.example.projecttest.helpComposeFiles.topNav

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.projecttest.R

@Composable
fun TopBar(
    onClick: () -> Unit = {},
    onOptionSelected: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp), // space between menu and avatar
        verticalAlignment = Alignment.CenterVertically
    ) {
        val filterOptions = listOf("About", "LogOut")
        var expanded by remember { mutableStateOf(false) }
        // Square background Icon
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(
                    color = Color.LightGray,
                    shape = RoundedCornerShape(8.dp) // or zero for square
                ),
            contentAlignment = Alignment.Center
        ) {
            IconButton(onClick = { expanded = true }) {
                Icon(Icons.Default.Menu, contentDescription = "Top Menu", tint = Color.Black)
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                filterOptions.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option) },
                        onClick = {
                            expanded = false
                            onOptionSelected(option)
                        }
                    )
                }
            }
        }

        // User avatar
        Image(
            painter = painterResource(R.drawable.user),
            contentDescription = "Avatar",
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .clickable { onClick() }
        )
    }
    Divider(
        color = Color.Gray,
        thickness = 1.dp,
        modifier = Modifier.padding(vertical = 8.dp)
    )
}