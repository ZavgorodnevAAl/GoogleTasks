package ru.zavgorodnev.googletasks.database

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.zavgorodnev.googletasks.data.task.Task

@Database(entities = [ Task::class ], version = 1, exportSchema = false)
abstract class TaskDatabase : RoomDatabase() {

    abstract fun taskDao(): TaskDao

}