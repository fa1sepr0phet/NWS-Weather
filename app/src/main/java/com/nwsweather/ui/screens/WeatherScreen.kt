package com.nwsweather.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Palette
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.nwsweather.data.local.SavedLocationEntity
import com.nwsweather.presentation.AppTheme
import com.nwsweather.presentation.WeatherUiState
import com.nwsweather.ui.components.ForecastCard
import com.nwsweather.ui.components.LocationSearchCard
import com.nwsweather.ui.components.SavedLocationChips
import com.nwsweather.ui.components.WeatherAtmosphereAnimation
import com.nwsweather.ui.components.WeatherHeroCard
import com.nwsweather.ui.theme.WeatherVisuals
import com.nwsweather.ui.theme.mapWeatherMood
import com.nwsweather.ui.theme.visualsForMood

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherScreen(
    uiState: WeatherUiState,
    onSearchQueryChanged: (String) -> Unit,
    onSaveLabelChanged: (String) -> Unit,
    onThemeChanged: (AppTheme) -> Unit,
    onSearchAddress: () -> Unit,
    onSavedLocationClick: (SavedLocationEntity) -> Unit,
    onRefresh: () -> Unit,
    onDismissError: () -> Unit,
    onUseCurrentLocation: () -> Unit,
    onDeleteLocation: (SavedLocationEntity) -> Unit,
    onEditLocation: (SavedLocationEntity) -> Unit,
    onStopEditing: () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }
    var showThemeMenu by remember { mutableStateOf(false) }
    var showSearchMenu by remember { mutableStateOf(false) }
    val sheetState = androidx.compose.material3.rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )
    val context = LocalContext.current

    val current = uiState.forecastResult?.currentPeriod

    val mood = mapWeatherMood(
        forecast = current?.shortForecast ?: "",
        isDay = current?.isDaytime ?: true
    )

    val openNwsWebsite = { lat: Double, lon: Double ->
        val url = "https://forecast.weather.gov/MapClick.php?lat=$lat&lon=$lon"
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        context.startActivity(intent)
    }

    val visuals = if (uiState.theme == AppTheme.MIDNIGHT) {
        WeatherVisuals(
            background = Brush.verticalGradient(listOf(Color.Black, Color.Black)),
            cardColor = Color(0xFF121212),
            onCardTextColor = Color.White,
            appBarContentColor = Color.White
        )
    } else {
        visualsForMood(mood)
    }

    LaunchedEffect(uiState.errorMessage) {
        val message = uiState.errorMessage ?: return@LaunchedEffect
        snackbarHostState.showSnackbar(message)
        onDismissError()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(visuals.background)
    ) {
        if (uiState.theme != AppTheme.MIDNIGHT) {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(
                        Brush.radialGradient(
                            colors = listOf(
                                Color.White.copy(alpha = 0.14f),
                                Color.Transparent
                            )
                        )
                    )
            )
        }

        WeatherAtmosphereAnimation(mood = mood)

        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = Color.Transparent,
            topBar = {
                CenterAlignedTopAppBar(
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = Color.Transparent,
                        scrolledContainerColor = Color.Transparent,
                        titleContentColor = visuals.appBarContentColor,
                        actionIconContentColor = visuals.appBarContentColor
                    ),
                    title = {
                        Text(
                            text = uiState.locationName ?: "NWS Weather",
                            style = MaterialTheme.typography.titleLarge
                        )
                    },
                    actions = {
                        IconButton(
                            onClick = { showSearchMenu = true }
                        ) {
                            Icon(
                                Icons.Default.Menu,
                                contentDescription = "Search menu"
                            )
                        }

                        if (showSearchMenu) {
                            ModalBottomSheet(
                                onDismissRequest = { showSearchMenu = false },
                                sheetState = sheetState,
                                containerColor = visuals.cardColor.copy(alpha = 0.95f),
                                contentColor = visuals.onCardTextColor
                            ) {
                                Column(
                                    modifier = Modifier
                                        .padding(16.dp)
                                        .padding(bottom = 32.dp),
                                    verticalArrangement = Arrangement.spacedBy(24.dp)
                                ) {
                                    val isEditing = uiState.editingLocation != null
                                    
                                    LocationSearchCard(
                                        searchQuery = uiState.searchQuery,
                                        saveLabel = uiState.saveLabel,
                                        isLoading = uiState.isLoading,
                                        onSearchQueryChanged = onSearchQueryChanged,
                                        onSaveLabelChanged = onSaveLabelChanged,
                                        onUseCurrentLocation = {
                                            onUseCurrentLocation()
                                            showSearchMenu = false
                                        },
                                        onSearchAddress = {
                                            onSearchAddress()
                                            if (isEditing) onStopEditing()
                                            showSearchMenu = false
                                        },
                                        hasAlerts = false,
                                        cardColor = Color.Transparent,
                                        textColor = visuals.onCardTextColor
                                    )

                                    if (isEditing) {
                                        androidx.compose.material3.TextButton(
                                            onClick = onStopEditing,
                                            modifier = Modifier.align(Alignment.CenterHorizontally)
                                        ) {
                                            Text("Cancel Editing", color = MaterialTheme.colorScheme.primary)
                                        }
                                    }

                                    if (uiState.savedLocations.isNotEmpty() && !isEditing) {
                                        Column(
                                            verticalArrangement = Arrangement.spacedBy(12.dp)
                                        ) {
                                            Text(
                                                text = "Manage saved locations",
                                                style = MaterialTheme.typography.titleMedium,
                                                color = visuals.onCardTextColor
                                            )

                                            uiState.savedLocations.forEach { location ->
                                                Row(
                                                    modifier = Modifier.fillMaxWidth(),
                                                    horizontalArrangement = Arrangement.SpaceBetween,
                                                    verticalAlignment = Alignment.CenterVertically
                                                ) {
                                                    Row(
                                                        verticalAlignment = Alignment.CenterVertically,
                                                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                                                        modifier = Modifier
                                                            .weight(1f)
                                                            .clickable {
                                                                onEditLocation(location)
                                                            }
                                                    ) {
                                                        Icon(
                                                            imageVector = Icons.Outlined.LocationOn,
                                                            contentDescription = null,
                                                            tint = visuals.onCardTextColor.copy(alpha = 0.6f)
                                                        )
                                                        Column {
                                                            Text(
                                                                text = location.label,
                                                                style = MaterialTheme.typography.bodyLarge,
                                                                color = visuals.onCardTextColor
                                                            )
                                                            Text(
                                                                text = location.address,
                                                                style = MaterialTheme.typography.bodySmall,
                                                                color = visuals.onCardTextColor.copy(alpha = 0.7f),
                                                                maxLines = 1
                                                            )
                                                        }
                                                    }

                                                    IconButton(
                                                        onClick = { onDeleteLocation(location) }
                                                    ) {
                                                        Icon(
                                                            imageVector = Icons.Default.Delete,
                                                            contentDescription = "Delete ${location.label}",
                                                            tint = MaterialTheme.colorScheme.error.copy(alpha = 0.8f)
                                                        )
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        IconButton(
                            onClick = { showThemeMenu = true }
                        ) {
                            Icon(
                                Icons.Outlined.Palette,
                                contentDescription = "Change theme"
                            )
                            DropdownMenu(
                                expanded = showThemeMenu,
                                onDismissRequest = { showThemeMenu = false }
                            ) {
                                DropdownMenuItem(
                                    text = { Text("System") },
                                    onClick = {
                                        onThemeChanged(AppTheme.SYSTEM)
                                        showThemeMenu = false
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text("Light") },
                                    onClick = {
                                        onThemeChanged(AppTheme.LIGHT)
                                        showThemeMenu = false
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text("Dark") },
                                    onClick = {
                                        onThemeChanged(AppTheme.DARK)
                                        showThemeMenu = false
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text("Midnight") },
                                    onClick = {
                                        onThemeChanged(AppTheme.MIDNIGHT)
                                        showThemeMenu = false
                                    }
                                )
                            }
                        }

                        IconButton(
                            onClick = onRefresh,
                            enabled = uiState.forecastResult != null && !uiState.isLoading
                        ) {
                            Icon(
                                Icons.Outlined.Refresh,
                                contentDescription = "Refresh forecast"
                            )
                        }
                    }
                )
            },
            snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
        ) { innerPadding ->
            PullToRefreshBox(
                isRefreshing = uiState.isLoading,
                onRefresh = onRefresh,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    if (uiState.forecastResult?.alerts?.isNotEmpty() == true) {
                        item {
                            ElevatedCard(
                                onClick = {
                                    uiState.forecastResult.let { result ->
                                        openNwsWebsite(result.latitude, result.longitude)
                                    }
                                },
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.elevatedCardColors(
                                    containerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.9f),
                                    contentColor = MaterialTheme.colorScheme.onErrorContainer
                                )
                            ) {
                                Row(
                                    modifier = Modifier.padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Warning,
                                        contentDescription = null,
                                        modifier = Modifier.size(24.dp),
                                        tint = MaterialTheme.colorScheme.error
                                    )
                                    Text(
                                        text = "Hazardous weather conditions reported",
                                        style = MaterialTheme.typography.labelLarge,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }

                    if (uiState.savedLocations.isNotEmpty()) {
                        item {
                            Text(
                                text = "Saved locations",
                                style = MaterialTheme.typography.titleMedium,
                                color = visuals.appBarContentColor
                            )
                        }
                        item {
                            SavedLocationChips(
                                locations = uiState.savedLocations,
                                onClick = onSavedLocationClick,
                                textColor = visuals.appBarContentColor
                            )
                        }
                    }

                    if (uiState.isLoading) {
                        item {
                            Text(
                                text = "Loading forecast…",
                                style = MaterialTheme.typography.bodyLarge,
                                color = visuals.appBarContentColor
                            )
                        }
                    }

                    val result = uiState.forecastResult
                    if (result != null) {
                        result.currentPeriod?.let { currentPeriod ->
                            item {
                                WeatherHeroCard(
                                    period = currentPeriod,
                                    locationName = result.locationName,
                                    cardColor = visuals.cardColor.copy(alpha = 0.6f),
                                    textColor = visuals.onCardTextColor,
                                    onClick = { openNwsWebsite(result.latitude, result.longitude) }
                                )
                            }
                        }

                        if (result.upcomingPeriods.isNotEmpty()) {
                            item {
                                HorizontalDivider(color = visuals.appBarContentColor.copy(alpha = 0.28f))
                            }
                            item {
                                Text(
                                    text = "Upcoming forecast",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = visuals.appBarContentColor
                                )
                            }
                            
                            val groupedPeriods = result.upcomingPeriods
                                .groupBy { it.name.removeSuffix(" Night") }
                                .values
                                .toList()

                            items(groupedPeriods) { periods ->
                                ForecastCard(
                                    periods = periods,
                                    cardColor = visuals.cardColor.copy(alpha = 0.4f),
                                    textColor = visuals.onCardTextColor,
                                    onClick = { openNwsWebsite(result.latitude, result.longitude) }
                                )
                            }
                        }
                    } else if (!uiState.isLoading) {
                        item {
                            Text(
                                text = "Search an address or use your current location to load a forecast.",
                                style = MaterialTheme.typography.bodyLarge,
                                color = visuals.appBarContentColor
                            )
                        }
                    }
                }
            }
        }
    }
}
