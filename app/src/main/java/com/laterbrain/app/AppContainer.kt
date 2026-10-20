package com.laterbrain.app

import android.content.Context
import com.laterbrain.app.data.ItemRepository
import com.laterbrain.app.data.LaterBrainDatabase

/** Creates the app's long-lived objects once. Screens get what they need from here. */
class AppContainer(context: Context) {
    private val database = LaterBrainDatabase.create(context)
    val repository = ItemRepository(database.savedItemDao())
}
