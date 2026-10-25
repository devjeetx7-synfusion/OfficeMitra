package com.officemitra.app.feature.holidays

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.officemitra.app.core.components.*

@Composable
fun HolidaysScreen(
    onBackClick: () -> Unit,
    viewModel: HolidaysViewModel = hiltViewModel()
) {
    val holidays by viewModel.holidays.collectAsState()
    var selectedFilter by remember { mutableStateOf("Upcoming") } // "Upcoming", "Past", "All"

    val filteredHolidays = holidays.filter { holiday ->
        when (selectedFilter) {
            "Upcoming" -> holiday.isUpcoming
            "Past" -> !holiday.isUpcoming
            else -> true
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        OfficeMitraTopBar(
            title = "Holiday Calendar",
            showBackButton = true,
            onBackClick = onBackClick
        )

        Column(modifier = Modifier.padding(16.dp)) {
            FilterChipRow(
                options = listOf("Upcoming", "Past", "All"),
                selectedOption = selectedFilter,
                onOptionSelected = { selectedFilter = it }
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (filteredHolidays.isEmpty()) {
                EmptyState(
                    title = "No Holidays Found",
                    description = "There are no $selectedFilter holidays recorded for this company."
                )
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(filteredHolidays) { holiday ->
                        HolidayCard(
                            title = holiday.name,
                            date = holiday.date,
                            dayOfWeek = holiday.dayOfWeek,
                            description = holiday.description,
                            isUpcoming = holiday.isUpcoming
                        )
                    }
                }
            }
        }
    }
}
