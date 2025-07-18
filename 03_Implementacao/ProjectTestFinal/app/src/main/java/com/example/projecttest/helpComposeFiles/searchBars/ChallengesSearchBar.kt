package com.example.projecttest.helpComposeFiles.searchBars

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ChallengesSearchBar(query: String,
              onQueryChange: (String) -> Unit,
              selectedFilter: String,
              onFilterSelected: (String) -> Unit,
              onSearchClick: () -> Unit
) {
    Row(
        modifier = Modifier.padding(horizontal = 16.dp).fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val filterOptions = listOf("Name", "Game")
        var expanded by remember { mutableStateOf(false) }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = query,
                onValueChange = onQueryChange,
                trailingIcon = {
                    IconButton(onClick = onSearchClick) {
                        Icon(Icons.Default.Search, contentDescription = "Search")
                    }
                },
                modifier = Modifier.weight(1f),
                singleLine = true,
                shape = RoundedCornerShape(12.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Box {
                OutlinedButton(
                    onClick = { expanded = true },
                    modifier = Modifier.height(56.dp) // match TextField height
                ) {
                    Text(selectedFilter)
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = "Filter Options"
                    )
                }

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    filterOptions.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option) },
                            onClick = {
                                onFilterSelected(option)
                                expanded = false
                            }
                        )
                    }
                }
            }
        }
    }
}