package com.example.stabletracker.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.stabletracker.data.entities.Horse
import com.example.stabletracker.data.entities.HorseActivity
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun HorseDetailScreen(
    horse: Horse,
    activities: List<HorseActivity>,
    onAddActivityClick: () -> Unit
) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = onAddActivityClick) {
                Text("+")
            }
        }
    ) { padding ->
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(padding)) {
            ListItem(
                headlineContent = { Text(horse.name, style = MaterialTheme.typography.titleLarge) },
                supportingContent = { horse.breed?.let { Text(it) } }
            )
            Divider()

            if (activities.isEmpty()) {
                Text(
                    text = "Активности пока нет",
                    modifier = Modifier.padding(16.dp)
                )
            } else {
                LazyColumn {
                    items(activities) { act ->
                        val df = remember { SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault()) }
                        ListItem(
                            headlineContent = { Text(act.type.toUiTitle()) },
                            supportingContent = {
                                Text(
                                    buildString {
                                        append(df.format(Date(act.timestampEpochMillis)))
                                        act.durationMinutes?.let { append("  •  ").append(it).append(" мин") }
                                        act.notes?.let { append("\n").append(it) }
                                    }
                                )
                            }
                        )
                        Divider()
                    }
                }
            }
        }
    }
}

private fun com.example.stabletracker.data.entities.ActivityType.toUiTitle(): String = when (this) {
    com.example.stabletracker.data.entities.ActivityType.FEEDING -> "Кормление"
    com.example.stabletracker.data.entities.ActivityType.PADDOCK -> "Левада"
    com.example.stabletracker.data.entities.ActivityType.TRAINING -> "Тренировка"
    com.example.stabletracker.data.entities.ActivityType.VET -> "Ветеринар"
    com.example.stabletracker.data.entities.ActivityType.OTHER -> "Другое"
}