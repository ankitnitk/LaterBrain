package com.laterbrain.app.data

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/** What kind of thing was saved. Auto-guessed when saving, the user can change it. */
enum class Category { RESTAURANT, RECIPE, ARTICLE, VIDEO, BOOK, PLACE, TRAVEL, SHOPPING, IDEA, OTHER }

/** The answer to "When should I remind you?". */
enum class ReminderWindow { TONIGHT, WEEKEND, NEARBY, NEXT_FREE, SURPRISE }

/** Where an item is in its life. */
enum class ItemStatus {
    /** Saved, waiting for the right moment. */
    WAITING,
    /** A reminder was shown, the user hasn't acted on it yet. */
    SHOWN,
    /** The user did the thing. */
    DONE,
    /** The user tapped "Later". */
    SNOOZED,
    /** The user tapped "Never again". */
    DISMISSED,
}

/**
 * One saved thing. Enums are stored by name (e.g. "RECIPE"), so never rename an enum value
 * without a database migration.
 */
@Entity(tableName = "saved_items", indices = [Index("status")])
data class SavedItem(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    /** The link, if one was shared. */
    val url: String? = null,
    /** Any extra text: a typed idea, or text that came along with a shared link. */
    val note: String? = null,
    val category: Category = Category.OTHER,
    val placeName: String? = null,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val reminderWindow: ReminderWindow = ReminderWindow.SURPRISE,
    val status: ItemStatus = ItemStatus.WAITING,
    val createdAt: Long = System.currentTimeMillis(),
    // Used by the resurfacing engine (Phase 2+). Added now to avoid a migration later.
    val lastShownAt: Long? = null,
    val snoozedUntil: Long? = null,
    val timesShown: Int = 0,
) {
    val isActive: Boolean
        get() = status == ItemStatus.WAITING || status == ItemStatus.SHOWN || status == ItemStatus.SNOOZED

    val hasPlace: Boolean
        get() = latitude != null && longitude != null
}
