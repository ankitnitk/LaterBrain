package com.laterbrain.app.ui.edit

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.Place
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.laterbrain.app.data.Category
import com.laterbrain.app.data.ReminderWindow
import com.laterbrain.app.ui.common.icon
import com.laterbrain.app.ui.common.label

/**
 * @param fromShare true when opened from the system Share menu: shows a close (X) instead of back.
 */
@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun EditItemScreen(
    viewModel: EditItemViewModel,
    fromShare: Boolean,
    onClose: () -> Unit,
    onSaved: (Long) -> Unit,
) {
    val state = viewModel.state
    val focus = remember { FocusRequester() }

    // For a brand-new typed note, put the cursor straight in the note box.
    LaunchedEffect(state.loaded) {
        if (state.loaded && state.isNew && !fromShare) runCatching { focus.requestFocus() }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        when {
                            fromShare -> "Save for later"
                            state.isNew -> "Quick add"
                            else -> "Edit"
                        }
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onClose) {
                        if (fromShare) Icon(Icons.Filled.Close, contentDescription = "Cancel")
                        else Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background),
            )
        },
        bottomBar = {
            Button(
                onClick = { viewModel.save(onSaved) },
                enabled = state.canSave,
                modifier = Modifier
                    .fillMaxWidth()
                    .imePadding()
                    .padding(horizontal = 20.dp, vertical = 16.dp)
                    .height(52.dp),
            ) { Text(if (state.isNew) "Save" else "Save changes") }
        },
    ) { padding ->
        if (!state.loaded) return@Scaffold
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            if (state.isNew && !fromShare) {
                OutlinedTextField(
                    value = state.note,
                    onValueChange = viewModel::onNoteChange,
                    label = { Text("What do you want to remember?") },
                    placeholder = { Text("e.g. Try the new Ethiopian place in Kilimani") },
                    keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences),
                    minLines = 3,
                    modifier = Modifier.fillMaxWidth().focusRequester(focus),
                )
                OutlinedTextField(
                    value = state.title,
                    onValueChange = viewModel::onTitleChange,
                    label = { Text("Short title (optional)") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Sentences,
                        imeAction = ImeAction.Next,
                    ),
                    modifier = Modifier.fillMaxWidth(),
                )
                OutlinedTextField(
                    value = state.link,
                    onValueChange = viewModel::onLinkChange,
                    label = { Text("Link (optional)") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Uri, imeAction = ImeAction.Done),
                    modifier = Modifier.fillMaxWidth(),
                )
            } else {
                OutlinedTextField(
                    value = state.title,
                    onValueChange = viewModel::onTitleChange,
                    label = { Text("Title") },
                    keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences),
                    modifier = Modifier.fillMaxWidth(),
                )
                OutlinedTextField(
                    value = state.link,
                    onValueChange = viewModel::onLinkChange,
                    label = { Text("Link") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Uri),
                    modifier = Modifier.fillMaxWidth(),
                )
                OutlinedTextField(
                    value = state.note,
                    onValueChange = viewModel::onNoteChange,
                    label = { Text("Note") },
                    keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences),
                    minLines = 2,
                    modifier = Modifier.fillMaxWidth(),
                )
            }

            state.placeName?.let { place ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Outlined.Place,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(18.dp),
                    )
                    Spacer(Modifier.size(6.dp))
                    Text(place, style = MaterialTheme.typography.bodyMedium)
                }
            }

            SectionLabel("When should I remind you?")
            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                ReminderWindow.entries.forEach { window ->
                    val selected = state.reminderWindow == window
                    FilterChip(
                        selected = selected,
                        onClick = { viewModel.onReminderWindowChange(window) },
                        label = { Text(window.label) },
                        leadingIcon = { Icon(window.icon, contentDescription = null, Modifier.size(18.dp)) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.tertiaryContainer,
                            selectedLabelColor = MaterialTheme.colorScheme.onTertiaryContainer,
                            selectedLeadingIconColor = MaterialTheme.colorScheme.onTertiaryContainer,
                        ),
                    )
                }
            }
            if (state.reminderWindow == ReminderWindow.NEARBY) {
                Text(
                    "Place reminders arrive in a coming update. Until then, I'll still bring this back at a good moment.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }

            SectionLabel("Category")
            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Category.entries.forEach { category ->
                    FilterChip(
                        selected = state.category == category,
                        onClick = { viewModel.onCategoryChange(category) },
                        label = { Text(category.label) },
                        leadingIcon = { Icon(category.icon, contentDescription = null, Modifier.size(18.dp)) },
                    )
                }
            }
            Spacer(Modifier.height(8.dp))
        }
    }
}

@Composable
private fun SectionLabel(text: String) {
    Text(
        text,
        style = MaterialTheme.typography.titleSmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier.padding(top = 6.dp),
    )
}
