package com.laterbrain.app.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.laterbrain.app.data.ItemRepository
import com.laterbrain.app.data.ItemStatus
import com.laterbrain.app.data.SavedItem
import com.laterbrain.app.domain.MaybeToday
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class HomeState(
    val maybeToday: SavedItem? = null,
    val waiting: List<SavedItem> = emptyList(),
    val finished: List<SavedItem> = emptyList(),
    val loading: Boolean = true,
) {
    val isEmpty: Boolean get() = !loading && maybeToday == null && waiting.isEmpty() && finished.isEmpty()
}

class HomeViewModel(private val repository: ItemRepository) : ViewModel() {

    val state: StateFlow<HomeState> = combine(repository.activeItems, repository.finishedItems) { active, finished ->
        val highlight = MaybeToday.pick(active)
        HomeState(
            maybeToday = highlight,
            waiting = active.filter { it.id != highlight?.id },
            finished = finished,
            loading = false,
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), HomeState())

    fun markDone(id: Long) = viewModelScope.launch { repository.setStatus(id, ItemStatus.DONE) }

    fun restore(id: Long) = viewModelScope.launch { repository.setStatus(id, ItemStatus.WAITING) }
}
