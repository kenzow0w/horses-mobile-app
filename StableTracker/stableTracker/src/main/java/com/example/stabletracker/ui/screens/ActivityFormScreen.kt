package com.example.stabletracker.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.example.stabletracker.data.entities.ActivityType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActivityFormScreen(
    onSave: (type: ActivityType, timestampEpochMillis: Long, durationMinutes: Int?, notes: String?) -> Unit,
    onCancel: () -> Unit
) {
    var selectedType by remember { mutableStateOf(ActivityType.FEEDING) }
    var minutesText by remember { mutableStateOf(TextFieldValue("")) }
    var notesText by remember { mutableStateOf(TextFieldValue("")) }

    // Время по умолчанию — сейчас
    val now = remember { System.currentTimeMillis() }

    Scaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("Новая активность", style = MaterialTheme.typography.titleLarge)

            ExposedDropdownMenuBox(
                expanded = false,
                onExpandedChange = {}
            ) {
                // Просто перечислим кнопками — легче и надёжнее для минималки
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    TypeChip("Кормление", ActivityType.FEEDING, selectedType) { selectedType = it }
                    TypeChip("Левада", ActivityType.PADDOCK, selectedType) { selectedType = it }
                    TypeChip("Тренировка", ActivityType.TRAINING, selectedType) { selectedType = it }
                }
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    TypeChip("Ветеринар", ActivityType.VET, selectedType) { selectedType = it }
                    TypeChip("Другое", ActivityType.OTHER, selectedType) { selectedType = it }
                }
            }

            OutlinedTextField(
                value = minutesText,
                onValueChange = { minutesText = it },
                label = { Text("Длительность (мин), необязательно") },
                singleLine = true
            )

            OutlinedTextField(
                value = notesText,
                onValueChange = { notesText = it },
                label = { Text("Заметки") }
            )

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Button(onClick = {
                    val minutes = minutesText.text.toIntOrNull()
                    onSave(selectedType, now, minutes, notesText.text.ifBlank { null })
                }) { Text("Сохранить") }
                OutlinedButton(onClick = onCancel) { Text("Отмена") }
            }
        }
    }
}

@Composable
private fun TypeChip(
    label: String,
    type: ActivityType,
    selected: ActivityType,
    onSelect: (ActivityType) -> Unit
) {
    FilterChip(
        selected = type == selected,
        onClick = { onSelect(type) },
        label = { Text(label) }
    )
}