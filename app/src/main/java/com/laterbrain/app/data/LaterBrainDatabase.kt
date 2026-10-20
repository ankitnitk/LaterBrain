package com.laterbrain.app.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [SavedItem::class], version = 1, exportSchema = true)
abstract class LaterBrainDatabase : RoomDatabase() {

    abstract fun savedItemDao(): SavedItemDao

    companion object {
        fun create(context: Context): LaterBrainDatabase =
            Room.databaseBuilder(context, LaterBrainDatabase::class.java, "laterbrain.db").build()
    }
}
