package ru.zavgorodnev.googletasks.ui.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.zavgorodnev.googletasks.data.task.InDatabaseTaskRepository
import ru.zavgorodnev.googletasks.data.task.Task

class ListViewModel : ViewModel() {

    private val tasksRepository = InDatabaseTaskRepository.get()

    val tasks: LiveData<List<Task>> = tasksRepository.getTasks()
    val favoriteTasks: LiveData<List<Task>> = tasksRepository.getFavouriteTasks()
    val completedTasks: LiveData<List<Task>> = tasksRepository.getCompletedTasks()

    fun isFavoriteButtonPressed(task: Task) = viewModelScope.launch(Dispatchers.IO) {
        tasksRepository.updateTask(task)
    }

    fun isCompletedButtonPressed(task: Task) = viewModelScope.launch(Dispatchers.IO) {
        tasksRepository.updateTask(task)
    }

    fun addTask(task: Task) = viewModelScope.launch(Dispatchers.IO) {
        tasksRepository.add(task)
    }

    fun updateTask(task: Task) = viewModelScope.launch(Dispatchers.IO) {
        tasksRepository.updateTask(task)
    }


}