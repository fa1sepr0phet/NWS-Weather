package com.example.nwsweather.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.MyLocation
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun LocationSearchCard(
    searchQuery: String,
    saveLabel: String,
    isLoading: Boolean,
    onSearchQueryChanged: (String) -> Unit,
    onSaveLabelChanged: (String) -> Unit,
    onUseCurrentLocation: () -> Unit,
    onSearchAddress: () -> Unit,
    cardColor: Color = Color.White.copy(alpha = 0.88f),
    textColor: Color = MaterialTheme.colorScheme.onSurface
) {
    val textFieldColors = OutlinedTextFieldDefaults.colors(
        focusedTextColor = textColor,
        unfocusedTextColor = textColor,
        focusedLabelColor = textColor.copy(alpha = 0.8f),
        unfocusedLabelColor = textColor.copy(alpha = 0.6f),
        focusedBorderColor = textColor.copy(alpha = 0.5f),
        unfocusedBorderColor = textColor.copy(alpha = 0.3f)
    )

    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.elevatedCardColors(
            containerColor = cardColor
        ),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 6.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Find weather",
                style = MaterialTheme.typography.titleLarge,
                color = textColor
            )

            OutlinedTextField(
                value = searchQuery,
                onValueChange = onSearchQueryChanged,
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Address") },
                singleLine = true,
                enabled = !isLoading,
                colors = textFieldColors
            )

            OutlinedTextField(
                value = saveLabel,
                onValueChange = onSaveLabelChanged,
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Save as label (optional)") },
                singleLine = true,
                enabled = !isLoading,
                colors = textFieldColors
            )

            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = onUseCurrentLocation,
                    enabled = !isLoading,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        imageVector = Icons.Outlined.MyLocation,
                        contentDescription = "Use current location"
                    )
                    Text(
                        text = "  Use current location",
                        style = MaterialTheme.typography.labelLarge
                    )
                }

                Button(
                    onClick = onSearchAddress,
                    enabled = !isLoading && searchQuery.isNotBlank(),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Search,
                        contentDescription = "Search address"
                    )
                    Text(
                        text = "  Search",
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }
        }
    }
}
