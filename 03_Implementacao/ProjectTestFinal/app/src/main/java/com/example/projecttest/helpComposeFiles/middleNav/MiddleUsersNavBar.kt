package com.example.projecttest.helpComposeFiles.middleNav

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
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.example.projecttest.R

@Composable
fun MiddleUsersNavBar(
    selectedIndex: Int,
    onTabSelected: (Int) -> Unit
) {
    val items = listOf(
        R.drawable.group,
        R.drawable.high_score,
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        contentAlignment = Alignment.CenterEnd
    ) {
        Row(
            modifier = Modifier
                .width(150.dp),
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
}
