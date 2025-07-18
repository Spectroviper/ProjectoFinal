package com.example.projecttest.achievementEditedCreated

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.projecttest.helpComposeFiles.bottomNav.BottomNavBar
import com.example.projecttest.helpComposeFiles.topNav.TopBar
import android.net.Uri

@Composable
fun AchievementCreatedEditedScreen(
    achievementName: String,
    onAchievementNameChange: (String) -> Unit,
    imageUrl: String,
    onImageUrlChange: (String) -> Unit,
    totalCollectable: Int?,
    onTotalCollectableChange: (Int) -> Unit,
    about: String,
    onAboutChange: (String) -> Unit,
    selectedTabIndex: Int = 1,
    onTabSelected: (Int) -> Unit = {},
    onAvatarClick: () -> Unit = {},
    onOptionSelected: (String) -> Unit,
    onFinishClick: () -> Unit
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
                .background(Color(0xFFE6ECFF))
        ) {
            TopBar(onAvatarClick, onOptionSelected)

            Spacer(Modifier.height(8.dp))

            Text("Achievement Name", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            OutlinedTextField(
                value = achievementName,
                onValueChange = onAchievementNameChange,
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

            Text("Total Collectable", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            OutlinedTextField(
                value = totalCollectable.toString(),
                onValueChange = { input ->
                    val number = input.toIntOrNull() ?: 0
                    onTotalCollectableChange(number)
                },
                textStyle = LocalTextStyle.current.copy(fontSize = 18.sp),
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            Text("About Achievement", fontSize = 20.sp, fontWeight = FontWeight.Bold)
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
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
            ) {
                Text("Finish", fontSize = 18.sp)
            }
        }
    }
}