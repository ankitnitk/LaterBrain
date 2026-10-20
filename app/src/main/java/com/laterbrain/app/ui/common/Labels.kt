package com.laterbrain.app.ui.common

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.MenuBook
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.Casino
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.EventAvailable
import androidx.compose.material.icons.outlined.Flight
import androidx.compose.material.icons.outlined.Kitchen
import androidx.compose.material.icons.outlined.Lightbulb
import androidx.compose.material.icons.outlined.NearMe
import androidx.compose.material.icons.outlined.Nightlight
import androidx.compose.material.icons.outlined.Place
import androidx.compose.material.icons.outlined.PlayCircle
import androidx.compose.material.icons.outlined.Restaurant
import androidx.compose.material.icons.outlined.ShoppingBag
import androidx.compose.material.icons.outlined.Weekend
import androidx.compose.ui.graphics.vector.ImageVector
import com.laterbrain.app.data.Category
import com.laterbrain.app.data.ItemStatus
import com.laterbrain.app.data.ReminderWindow

val Category.label: String
    get() = when (this) {
        Category.RESTAURANT -> "Restaurant"
        Category.RECIPE -> "Recipe"
        Category.ARTICLE -> "Article"
        Category.VIDEO -> "Video"
        Category.BOOK -> "Book"
        Category.PLACE -> "Place"
        Category.TRAVEL -> "Travel"
        Category.SHOPPING -> "Shopping"
        Category.IDEA -> "Idea"
        Category.OTHER -> "Other"
    }

val Category.icon: ImageVector
    get() = when (this) {
        Category.RESTAURANT -> Icons.Outlined.Restaurant
        Category.RECIPE -> Icons.Outlined.Kitchen
        Category.ARTICLE -> Icons.Outlined.Description
        Category.VIDEO -> Icons.Outlined.PlayCircle
        Category.BOOK -> Icons.AutoMirrored.Outlined.MenuBook
        Category.PLACE -> Icons.Outlined.Place
        Category.TRAVEL -> Icons.Outlined.Flight
        Category.SHOPPING -> Icons.Outlined.ShoppingBag
        Category.IDEA -> Icons.Outlined.Lightbulb
        Category.OTHER -> Icons.Outlined.BookmarkBorder
    }

val ReminderWindow.label: String
    get() = when (this) {
        ReminderWindow.TONIGHT -> "Tonight"
        ReminderWindow.WEEKEND -> "Weekend"
        ReminderWindow.NEARBY -> "When I'm nearby"
        ReminderWindow.NEXT_FREE -> "Next time I'm free"
        ReminderWindow.SURPRISE -> "Surprise me"
    }

val ReminderWindow.icon: ImageVector
    get() = when (this) {
        ReminderWindow.TONIGHT -> Icons.Outlined.Nightlight
        ReminderWindow.WEEKEND -> Icons.Outlined.Weekend
        ReminderWindow.NEARBY -> Icons.Outlined.NearMe
        ReminderWindow.NEXT_FREE -> Icons.Outlined.EventAvailable
        ReminderWindow.SURPRISE -> Icons.Outlined.Casino
    }

val ItemStatus.label: String
    get() = when (this) {
        ItemStatus.WAITING -> "Waiting for the right moment"
        ItemStatus.SHOWN -> "Reminded you recently"
        ItemStatus.DONE -> "Done"
        ItemStatus.SNOOZED -> "Snoozed"
        ItemStatus.DISMISSED -> "Not interested"
    }

/** "Saved today", "Saved 3 days ago", "Saved 2 months ago". */
fun savedAgo(createdAt: Long, now: Long = System.currentTimeMillis()): String {
    val days = ((now - createdAt) / 86_400_000L).coerceAtLeast(0)
    return when {
        days == 0L -> "Saved today"
        days == 1L -> "Saved yesterday"
        days < 30 -> "Saved $days days ago"
        days < 60 -> "Saved a month ago"
        days < 365 -> "Saved ${days / 30} months ago"
        else -> "Saved over a year ago"
    }
}
