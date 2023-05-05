package ru.zavgorodnev.googletasks.data.task

data class Task(
    var title: String,
    var description: String,
    var isCompleted: Boolean,
    var isFavorite: Boolean
)