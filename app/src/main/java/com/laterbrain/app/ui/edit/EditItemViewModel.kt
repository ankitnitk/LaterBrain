package com.laterbrain.app.ui.edit

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.laterbrain.app.capture.CategoryGuesser
import com.laterbrain.app.capture.SharedContent
import com.laterbrain.app.data.Category
import com.laterbrain.app.data.ItemRepository
import com.laterbrain.app.data.ReminderWindow
import com.laterbrain.app.data.SavedItem
import kotlinx.coroutines.launch

data class EditState(
    val title: String = "",
    val link: String = "",
    val note: String = "",
    val category: Category = Category.IDEA,
    val reminderWindow: ReminderWindow = ReminderWindow.SURPRISE,
    val placeName: String? = null,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val isNew: Boolean = true,
    val loaded: Boolean = false,
) {
    val canSave: Boolean get() = title.isNotBlank() || link.isNotBlank() || note.isNotBlank()
}

/**
 * Holds what's typed on the Add/Edit screen.
 * @param itemId an existing item to edit, or null to create a new one.
 * @param shared content shared in from another app, used to pre-fill a new item.
 */
class EditItemViewModel(
    private val repository: ItemRepository,
    private val itemId: Long?,
    shared: SharedContent?,
) : ViewModel() {

    var state by mutableStateOf(EditState())
        private set

    private var original: SavedItem? = null

    /** Once the user picks a category themselves, we stop auto-guessing. */
    private var categoryChosenByUser = false

    init {
        when {
            itemId != null -> viewModelScope.launch {
                val item = repository.get(itemId)
                original = item
                categoryChosenByUser = true
                state = if (item == null) EditState(loaded = true) else EditState(
                    title = item.title,
                    link = item.url.orEmpty(),
                    note = item.note.orEmpty(),
                    category = item.category,
                    reminderWindow = item.reminderWindow,
                    placeName = item.placeName,
                    latitude = item.latitude,
                    longitude = item.longitude,
                    isNew = false,
                    loaded = true,
                )
            }
            shared != null -> state = EditState(
                title = shared.title,
                link = shared.url.orEmpty(),
                note = shared.note.orEmpty(),
                category = CategoryGuesser.guess(shared.title, shared.url, shared.note),
                placeName = shared.placeName,
                latitude = shared.latitude,
                longitude = shared.longitude,
                loaded = true,
            )
            else -> state = EditState(loaded = true)
        }
    }

    fun onTitleChange(value: String) = updateAndGuess(state.copy(title = value))
    fun onLinkChange(value: String) = updateAndGuess(state.copy(link = value))
    fun onNoteChange(value: String) = updateAndGuess(state.copy(note = value))

    fun onCategoryChange(value: Category) {
        categoryChosenByUser = true
        state = state.copy(category = value)
    }

    fun onReminderWindowChange(value: ReminderWindow) {
        state = state.copy(reminderWindow = value)
    }

    private fun updateAndGuess(newState: EditState) {
        state = if (categoryChosenByUser) newState else newState.copy(
            category = CategoryGuesser.guess(newState.title, newState.link.ifBlank { null }, newState.note)
        )
    }

    fun save(onSaved: (Long) -> Unit) {
        val s = state
        if (!s.canSave) return
        val link = s.link.trim().ifBlank { null }
        val note = s.note.trim().ifBlank { null }
        // A typed note with no title becomes its own title.
        val title = s.title.trim().ifBlank { note?.lineSequence()?.first()?.take(90) ?: link.orEmpty() }
        // Don't store the same words twice when a short note became the title.
        val finalNote = note?.takeIf { it != title }

        val item = (original ?: SavedItem(title = title)).copy(
            title = title,
            url = link,
            note = finalNote,
            category = s.category,
            reminderWindow = s.reminderWindow,
            placeName = s.placeName,
            latitude = s.latitude,
            longitude = s.longitude,
        )
        viewModelScope.launch { onSaved(repository.save(item)) }
    }
}
