package ru.zavgorodnev.googletasks.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.zavgorodnev.googletasks.data.task.InDatabaseTaskRepository
import ru.zavgorodnev.googletasks.data.task.Task

class MainViewModel: ViewModel() {

    private val taskRepository = InDatabaseTaskRepository.get()

    fun onAddTaskButtonPressed(task: Task) = viewModelScope.launch(Dispatchers.IO) {
        taskRepository.add(task)
    }

}