package com.example.stabletracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.stabletracker.data.db.StableDatabase
import com.example.stabletracker.data.repository.StableRepository
import com.example.stabletracker.ui.screens.ActivityFormScreen
import com.example.stabletracker.ui.screens.HorseDetailScreen
import com.example.stabletracker.ui.screens.HorseListScreen
import com.example.stabletracker.ui.theme.StableTrackerTheme
import com.example.stabletracker.viewmodel.HorseDetailViewModel
import com.example.stabletracker.viewmodel.HorseListViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { App() }
    }
}

@Composable
fun App() {
    StableTrackerTheme {
        val navController = rememberNavController()
        val appContext = LocalContext.current.applicationContext

        val repository = remember {
            val db = StableDatabase.getInstance(context = appContext)
            StableRepository(db.horseDao(), db.horseActivityDao())
        }

        NavHost(navController = navController, startDestination = "horses") {
            composable("horses") {
                val vm: HorseListViewModel = viewModel(factory = object : ViewModelProvider.Factory {
                    override fun <T : ViewModel> create(modelClass: Class<T>): T {
                        @Suppress("UNCHECKED_CAST")
                        return HorseListViewModel(repository) as T
                    }
                })
                val horses by vm.horses.collectAsState()

                HorseListScreen(
                    horses = horses,
                    onHorseClick = { horse -> navController.navigate("horse/${horse.id}") },
                    onAddHorse = { name, breed -> vm.addHorse(name, breed) }
                )
            }

            composable(
                route = "horse/{horseId}",
                arguments = listOf(navArgument("horseId") { type = NavType.LongType })
            ) { backStackEntry ->
                val horseId = backStackEntry.arguments?.getLong("horseId") ?: return@composable
                val vm: HorseDetailViewModel = viewModel(factory = object : ViewModelProvider.Factory {
                    override fun <T : ViewModel> create(modelClass: Class<T>): T {
                        @Suppress("UNCHECKED_CAST")
                        return HorseDetailViewModel(horseId, repository) as T
                    }
                })
                val horse by vm.horse.collectAsState()
                val activities by vm.activities.collectAsState(initial = emptyList())

                horse?.let {
                    HorseDetailScreen(
                        horse = it,
                        activities = activities,
                        onAddActivityClick = {
                            navController.navigate("horse/${horseId}/addActivity")
                        }
                    )
                } ?: Text("Лошадь не найдена", style = MaterialTheme.typography.bodyLarge)
            }

            composable(
                route = "horse/{horseId}/addActivity",
                arguments = listOf(navArgument("horseId") { type = NavType.LongType })
            ) { backStackEntry ->
                val horseId = backStackEntry.arguments?.getLong("horseId") ?: return@composable
                val vm: HorseDetailViewModel = viewModel(factory = object : ViewModelProvider.Factory {
                    override fun <T : ViewModel> create(modelClass: Class<T>): T {
                        @Suppress("UNCHECKED_CAST")
                        return HorseDetailViewModel(horseId, repository) as T
                    }
                })

                ActivityFormScreen(
                    onSave = { type, ts, mins, notes ->
                        vm.addActivity(type, ts, mins, notes)
                        navController.popBackStack()
                    },
                    onCancel = { navController.popBackStack() }
                )
            }
        }
    }
}