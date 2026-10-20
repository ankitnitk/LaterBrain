package com.laterbrain.app.domain

import com.laterbrain.app.data.Category
import com.laterbrain.app.data.ItemStatus
import com.laterbrain.app.data.ReminderWindow
import com.laterbrain.app.data.SavedItem
import java.time.DayOfWeek
import java.time.LocalDateTime

/**
 * Picks the one item to highlight on the home screen as "Maybe today".
 * A deliberately simple first version; Phase 3 replaces it with the full scoring rules.
 */
object MaybeToday {

    fun pick(items: List<SavedItem>, now: LocalDateTime = LocalDateTime.now()): SavedItem? {
        val candidates = items.filter { it.status == ItemStatus.WAITING || it.status == ItemStatus.SHOWN }
        // Highest score wins; ties go to the item that has been waiting longest.
        return candidates.maxWithOrNull(
            compareBy<SavedItem> { score(it, now) }.thenByDescending { it.createdAt }
        )
    }

    fun score(item: SavedItem, now: LocalDateTime): Int {
        val hour = now.hour
        val weekend = now.dayOfWeek == DayOfWeek.SATURDAY || now.dayOfWeek == DayOfWeek.SUNDAY
        val evening = hour in 17..21
        var score = 0

        when (item.reminderWindow) {
            ReminderWindow.TONIGHT -> if (evening) score += 3
            ReminderWindow.WEEKEND -> if (weekend) score += 3
            ReminderWindow.NEXT_FREE -> if (weekend || evening) score += 2
            ReminderWindow.SURPRISE -> score += 1
            ReminderWindow.NEARBY -> Unit // Needs location, which arrives in v1.1.
        }
        when (item.category) {
            Category.RECIPE -> if (weekend && hour in 8..12) score += 2
            Category.ARTICLE, Category.BOOK, Category.VIDEO -> if (evening) score += 1
            Category.RESTAURANT -> if (weekend || hour in 11..13 || evening) score += 1
            else -> Unit
        }
        return score
    }
}
