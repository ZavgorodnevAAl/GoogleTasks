package ru.zavgorodnev.googletasks.data.task

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity
data class Task(
    @PrimaryKey val id: UUID = UUID.randomUUID(),
    var title: String,
    var description: String,
    var isCompleted: Boolean,
    var isFavorite: Boolean,
    var parent: UUID?
)