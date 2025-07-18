package com.example.projecttest.gameEditedCreated

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.projecttest.helpComposeFiles.bottomNav.BottomNavBar
import com.example.projecttest.helpComposeFiles.topNav.TopBar
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.ui.layout.ContentScale
import coil.compose.rememberAsyncImagePainter

@Composable
fun GameCreatedEditedScreen(
    gameName: String,
    onGameNameChange: (String) -> Unit,
    imageUrl: String,
    onImageUrlChange: (String) -> Unit,
    console: String,
    onConsoleChange: (String) -> Unit,
    developer: String,
    onDeveloperChange: (String) -> Unit,
    publisher: String,
    onPublisherChange: (String) -> Unit,
    genre: String,
    onGenreChange: (String) -> Unit,
    releaseDate: String,
    onReleaseDateChange: (String) -> Unit,
    about: String,
    onAboutChange: (String) -> Unit,
    createdBy: String,
    onCreatedBy: (String) -> Unit,
    selectedTabIndex: Int = 1,
    onTabSelected: (Int) -> Unit = {},
    onAvatarClick: () -> Unit = {},
    onOptionSelected: (String) -> Unit,
    onFinishClick: () -> Unit,
    onPickDateClick: () -> Unit
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

            Spacer(Modifier.height(8.dp))

            Text("Game Name", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            OutlinedTextField(
                value = gameName,
                onValueChange = onGameNameChange,
                textStyle = LocalTextStyle.current.copy(fontSize = 18.sp),
                modifier = Modifier.fillMaxWidth()
            )

            val context = LocalContext.current
            val imagePickerLauncher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.GetContent()
            ) { uri: Uri? ->
                uri?.let { onImageUrlChange(it.toString()) }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(Color.Gray)
                    .clickable { imagePickerLauncher.launch("image/*") },
                contentAlignment = Alignment.Center
            ) {
                if (imageUrl.isNotBlank()) {
                    Image(
                        painter = rememberAsyncImagePainter(imageUrl),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Text("Tap to select image", color = Color.White)
                }
            }

            Text("Console", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            OutlinedTextField(
                value = console,
                onValueChange = onConsoleChange,
                textStyle = LocalTextStyle.current.copy(fontSize = 18.sp),
                modifier = Modifier.fillMaxWidth()
            )

            Text("Developer", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            OutlinedTextField(
                value = developer,
                onValueChange = onDeveloperChange,
                textStyle = LocalTextStyle.current.copy(fontSize = 18.sp),
                modifier = Modifier.fillMaxWidth()
            )

            Text("Publisher", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            OutlinedTextField(
                value = publisher,
                onValueChange = onPublisherChange,
                textStyle = LocalTextStyle.current.copy(fontSize = 18.sp),
                modifier = Modifier.fillMaxWidth()
            )

            Text("Genre", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            OutlinedTextField(
                value = genre,
                onValueChange = onGenreChange,
                textStyle = LocalTextStyle.current.copy(fontSize = 18.sp),
                modifier = Modifier.fillMaxWidth()
            )

            Text("Release Date", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = releaseDate,
                    onValueChange = onReleaseDateChange,
                    textStyle = LocalTextStyle.current.copy(fontSize = 18.sp),
                    modifier = Modifier.weight(1f)
                )
                Spacer(Modifier.width(8.dp))
                Button(onClick = onPickDateClick) {
                    Text("Pick Date")
                }
            }

            Text("About Game", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            OutlinedTextField(
                value = about,
                onValueChange = onAboutChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                textStyle = LocalTextStyle.current.copy(fontSize = 18.sp),
                maxLines = 5
            )

            Button(
                onClick = onFinishClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)) // green
            ) {
                Text("Finish", fontSize = 18.sp)
            }
        }
    }
}