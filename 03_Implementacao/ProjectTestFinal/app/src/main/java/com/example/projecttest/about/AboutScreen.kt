package com.example.projecttest.about

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.projecttest.helpComposeFiles.bottomNav.BottomNavBar
import com.example.projecttest.helpComposeFiles.topNav.TopBar

@Composable
fun AboutScreen(
    selectedTabIndex: Int = 1,
    onTabSelected: (Int) -> Unit = {},
    onAvatarClick: () -> Unit = {},
    onOptionSelected: (String) -> Unit,
) {
    Scaffold(
        bottomBar = {
            BottomNavBar(
                selectedIndex = selectedTabIndex,
                onTabSelected = onTabSelected
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(paddingValues)
                .background(Color(0xFFE6ECFF)) // light blue

        ) {
            TopBar(onAvatarClick, onOptionSelected)
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "About",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "This App was made for our Final Project." +
                        "The project consists of creating an Android app using Kotlin as the programming language and an API in Node.js, using the technologies GraphQL and TypeScript.\n" +
                        "The goal of the application is to simulate the process of tracking achievements created by and for players in various video games, and to allow those players to create challenges where multiple users can compete with each other.\n" +
                        "To that end, a system was developed so that users can create, add, and unlock achievements related to various games.\n" +
                        "Users have the freedom to explore the app by creating and unlocking achievements in games they want to compete in.\n" +
                        "The motivation for this project was to create an application that enables playing these older video games in a different way, by creating challenges beyond what the original developers may have envisioned.",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Left
            )

            Spacer(modifier = Modifier.height(32.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                // Made by
                Column(
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(text = "Made by", style = MaterialTheme.typography.labelLarge)
                    Text(text = "Rui Lança", style = MaterialTheme.typography.bodySmall)
                    Text(text = "Pedro Gomes", style = MaterialTheme.typography.bodySmall)
                }

                // Adviser
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "Adviser", style = MaterialTheme.typography.labelLarge)
                    Text(text = "Ana Correia", style = MaterialTheme.typography.bodySmall)
                }

                // Special Thanks
                Column(
                    horizontalAlignment = Alignment.End
                ) {
                    Text(text = "Special Thanks", style = MaterialTheme.typography.labelLarge)
                    Text(text = "Pedro Fazenda", style = MaterialTheme.typography.bodySmall)
                }
            }

        }
    }
}