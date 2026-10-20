package com.laterbrain.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.laterbrain.app.data.ItemRepository
import com.laterbrain.app.ui.detail.ItemDetailScreen
import com.laterbrain.app.ui.detail.ItemDetailViewModel
import com.laterbrain.app.ui.edit.EditItemScreen
import com.laterbrain.app.ui.edit.EditItemViewModel
import com.laterbrain.app.ui.home.HomeScreen
import com.laterbrain.app.ui.home.HomeViewModel
import com.laterbrain.app.ui.theme.LaterBrainTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val repository = appContainer.repository
        setContent {
            LaterBrainTheme {
                LaterBrainNavHost(repository)
            }
        }
    }
}

private object Routes {
    const val HOME = "home"
    const val ADD = "add"
    const val DETAIL = "detail/{id}"
    const val EDIT = "edit/{id}"
    fun detail(id: Long) = "detail/$id"
    fun edit(id: Long) = "edit/$id"
}

@Composable
private fun LaterBrainNavHost(repository: ItemRepository) {
    val nav = rememberNavController()
    val idArg = listOf(navArgument("id") { type = NavType.LongType })

    NavHost(navController = nav, startDestination = Routes.HOME) {
        composable(Routes.HOME) {
            HomeScreen(
                viewModel = viewModel { HomeViewModel(repository) },
                onAdd = { nav.navigate(Routes.ADD) },
                onOpen = { id -> nav.navigate(Routes.detail(id)) },
            )
        }
        composable(Routes.ADD) {
            EditItemScreen(
                viewModel = viewModel { EditItemViewModel(repository, itemId = null, shared = null) },
                fromShare = false,
                onClose = { nav.popBackStack() },
                onSaved = { nav.popBackStack() },
            )
        }
        composable(Routes.DETAIL, arguments = idArg) { entry ->
            val id = entry.arguments!!.getLong("id")
            ItemDetailScreen(
                viewModel = viewModel { ItemDetailViewModel(repository, id) },
                onBack = { nav.popBackStack() },
                onEdit = { nav.navigate(Routes.edit(it)) },
            )
        }
        composable(Routes.EDIT, arguments = idArg) { entry ->
            val id = entry.arguments!!.getLong("id")
            EditItemScreen(
                viewModel = viewModel { EditItemViewModel(repository, itemId = id, shared = null) },
                fromShare = false,
                onClose = { nav.popBackStack() },
                onSaved = { nav.popBackStack() },
            )
        }
    }
}
