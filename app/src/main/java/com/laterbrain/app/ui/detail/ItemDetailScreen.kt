package com.laterbrain.app.ui.detail

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.OpenInNew
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Place
import androidx.compose.material.icons.automirrored.outlined.Undo
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import com.laterbrain.app.data.ItemRepository
import com.laterbrain.app.data.ItemStatus
import com.laterbrain.app.data.SavedItem
import com.laterbrain.app.ui.common.icon
import com.laterbrain.app.ui.common.label
import com.laterbrain.app.ui.common.savedAgo
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/** Loading while the database answers; Missing if the item was deleted. */
sealed interface DetailState {
    data object Loading : DetailState
    data object Missing : DetailState
    data class Loaded(val item: SavedItem) : DetailState
}

class ItemDetailViewModel(private val repository: ItemRepository, private val itemId: Long) : ViewModel() {

    val state: StateFlow<DetailState> = repository.observe(itemId)
        .map { if (it == null) DetailState.Missing else DetailState.Loaded(it) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), DetailState.Loading)

    fun setStatus(status: ItemStatus) = viewModelScope.launch { repository.setStatus(itemId, status) }

    fun delete(onDeleted: () -> Unit) = viewModelScope.launch {
        repository.delete(itemId)
        onDeleted()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemDetailScreen(
    viewModel: ItemDetailViewModel,
    onBack: () -> Unit,
    onEdit: (Long) -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    var confirmDelete by remember { mutableStateOf(false) }
    val item = (state as? DetailState.Loaded)?.item

    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    if (item != null) {
                        IconButton(onClick = { onEdit(item.id) }) {
                            Icon(Icons.Outlined.Edit, contentDescription = "Edit")
                        }
                        IconButton(onClick = { confirmDelete = true }) {
                            Icon(Icons.Outlined.Delete, contentDescription = "Delete")
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background),
            )
        },
    ) { padding ->
        when (val s = state) {
            DetailState.Loading -> Unit
            DetailState.Missing -> Text(
                "This item is no longer here.",
                modifier = Modifier.padding(padding).padding(24.dp),
            )
            is DetailState.Loaded -> DetailContent(
                item = s.item,
                onMarkDone = { viewModel.setStatus(ItemStatus.DONE) },
                onRestore = { viewModel.setStatus(ItemStatus.WAITING) },
                modifier = Modifier.padding(padding),
            )
        }
    }

    if (confirmDelete) {
        AlertDialog(
            onDismissRequest = { confirmDelete = false },
            title = { Text("Delete this item?") },
            text = { Text("It will be removed from your phone for good.") },
            confirmButton = {
                TextButton(onClick = {
                    confirmDelete = false
                    viewModel.delete(onBack)
                }) { Text("Delete") }
            },
            dismissButton = { TextButton(onClick = { confirmDelete = false }) { Text("Keep") } },
        )
    }
}

@Composable
private fun DetailContent(
    item: SavedItem,
    onMarkDone: () -> Unit,
    onRestore: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current

    fun openUrl(url: String) {
        val uri = Uri.parse(if (url.startsWith("http", ignoreCase = true)) url else "https://$url")
        try {
            context.startActivity(Intent(Intent.ACTION_VIEW, uri))
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(context, "No app found to open this link", Toast.LENGTH_SHORT).show()
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            AssistChip(
                onClick = {},
                label = { Text(item.category.label) },
                leadingIcon = { Icon(item.category.icon, contentDescription = null, Modifier.size(18.dp)) },
            )
            AssistChip(
                onClick = {},
                label = { Text(item.reminderWindow.label) },
                leadingIcon = { Icon(item.reminderWindow.icon, contentDescription = null, Modifier.size(18.dp)) },
            )
        }

        SelectionContainer {
            Text(item.title, style = MaterialTheme.typography.headlineSmall)
        }
        Text(
            "${savedAgo(item.createdAt)} · ${item.status.label}",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        item.placeName?.let { place ->
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Outlined.Place, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                Spacer(Modifier.width(6.dp))
                Text(place, style = MaterialTheme.typography.bodyLarge)
            }
        }

        item.note?.let { note ->
            SelectionContainer {
                Text(note, style = MaterialTheme.typography.bodyLarge)
            }
        }

        item.url?.let { url ->
            Text(
                url,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 2,
            )
        }

        Spacer(Modifier.height(8.dp))

        item.url?.let { url ->
            Button(onClick = { openUrl(url) }, modifier = Modifier.fillMaxWidth().height(52.dp)) {
                Icon(Icons.AutoMirrored.Outlined.OpenInNew, contentDescription = null, Modifier.size(18.dp))
                Spacer(Modifier.width(8.dp))
                Text("Open link")
            }
        }

        if (item.isActive) {
            OutlinedButton(onClick = onMarkDone, modifier = Modifier.fillMaxWidth().height(52.dp)) {
                Icon(Icons.Outlined.CheckCircle, contentDescription = null, Modifier.size(18.dp))
                Spacer(Modifier.width(8.dp))
                Text("Mark as done")
            }
        } else {
            OutlinedButton(onClick = onRestore, modifier = Modifier.fillMaxWidth().height(52.dp)) {
                Icon(Icons.AutoMirrored.Outlined.Undo, contentDescription = null, Modifier.size(18.dp))
                Spacer(Modifier.width(8.dp))
                Text("Bring it back")
            }
        }
        Spacer(Modifier.height(24.dp))
    }
}
