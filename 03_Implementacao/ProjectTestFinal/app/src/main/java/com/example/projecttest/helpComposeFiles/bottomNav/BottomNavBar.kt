package com.example.projecttest.helpComposeFiles.bottomNav

import androidx.compose.foundation.Image
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.projecttest.R

/*@Composable
fun BottomNavBar(selectedTab: String) {
    NavigationBar {
        NavigationBarItem(selected = selectedTab == "Library", onClick = {}, icon = {
            Icon(imageVector = ImageVector.vectorResource(id = R.drawable.magic_book), contentDescription = null)
        }, label = { Text("Library") })

        NavigationBarItem(selected = selectedTab == "Games", onClick = {}, icon = {
            Icon(imageVector = ImageVector.vectorResource(id = R.drawable.game_controller), contentDescription = null)
        }, label = { Text("Games") })

        NavigationBarItem(selected = selectedTab == "Users", onClick = {}, icon = {
            Icon(imageVector = ImageVector.vectorResource(id = R.drawable.group), contentDescription = null)
        }, label = { Text("Users") })

        NavigationBarItem(selected = selectedTab == "Users", onClick = {}, icon = {
            Icon(imageVector = ImageVector.vectorResource(id = R.drawable.game), contentDescription = null)
        }, label = { Text("Friend List") })

        NavigationBarItem(selected = selectedTab == "Challenges", onClick = {}, icon = {
            Icon(imageVector = ImageVector.vectorResource(id = R.drawable.goal), contentDescription = null)
        }, label = { Text("Challenges") })
    }
}*/
@Composable
fun BottomNavBar(
    selectedIndex: Int,
    onTabSelected: (Int) -> Unit
) {
    val items = listOf(
        R.drawable.magic_book,
        R.drawable.game_controller,
        R.drawable.group,
        R.drawable.game,
        R.drawable.goal
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .background(Color.White),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        items.forEachIndexed { index, drawableRes ->
            val isSelected = index == selectedIndex

            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(if (isSelected) Color(0xFF2196F3).copy(alpha = 0.2f) else Color.Transparent)
                    .padding(8.dp)
                    .clickable { onTabSelected(index) },
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = drawableRes),
                    contentDescription = "Tab $index",
                    modifier = Modifier.size(32.dp)
                )
            }
        }
    }
}
