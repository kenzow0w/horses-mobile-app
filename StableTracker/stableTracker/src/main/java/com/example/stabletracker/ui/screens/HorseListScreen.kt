package com.example.stabletracker.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.example.stabletracker.data.entities.Horse

@Composable
fun HorseListScreen(
    horses: List<Horse>,
    onHorseClick: (Horse) -> Unit,
    onAddHorse: (name: String, breed: String?) -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { showDialog = true }) {
                Text("+")
            }
        }
    ) { padding ->
        if (horses.isEmpty()) {
            Box(modifier = Modifier
                .fillMaxSize()
                .padding(padding), contentAlignment = Alignment.Center) {
                Text("Нет лошадей. Нажмите + чтобы добавить.")
            }
        } else {
            LazyColumn(modifier = Modifier
                .fillMaxSize()
                .padding(padding)) {
                items(horses) { horse ->
                    ListItem(
                        headlineContent = { Text(horse.name) },
                        supportingContent = { horse.breed?.let { Text(it) } },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp)
                            .clickable { onHorseClick(horse) }
                    )
                    Divider()
                }
            }
        }
    }

    if (showDialog) {
        AddHorseDialog(
            onDismiss = { showDialog = false },
            onAdd = { name, breed ->
                onAddHorse(name, breed)
                showDialog = false
            }
        )
    }
}

@Composable
private fun AddHorseDialog(
    onDismiss: () -> Unit,
    onAdd: (String, String?) -> Unit
) {
    var name by remember { mutableStateOf(TextFieldValue("")) }
    var breed by remember { mutableStateOf(TextFieldValue("")) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Добавить лошадь") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Имя") },
                    singleLine = true
                )
                OutlinedTextField(
                    value = breed,
                    onValueChange = { breed = it },
                    label = { Text("Порода (необязательно)") },
                    singleLine = true
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = { if (name.text.isNotBlank()) onAdd(name.text.trim(), breed.text.ifBlank { null }) }
            ) { Text("Добавить") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Отмена") } }
    )
}