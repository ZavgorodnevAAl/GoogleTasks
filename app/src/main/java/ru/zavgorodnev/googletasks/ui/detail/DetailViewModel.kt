package ru.zavgorodnev.googletasks.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.zavgorodnev.googletasks.data.task.InDatabaseTaskRepository
import ru.zavgorodnev.googletasks.data.task.Task
import java.util.UUID

class DetailViewModel : ViewModel() {
    private val taskRepository = InDatabaseTaskRepository.get()
    private val taskId = MutableLiveData<UUID>()
    val task: LiveData<Task?> = Transformations.switchMap(taskId) { taskId ->
        taskRepository.getTask(taskId)
    }

    fun load(_taskId: UUID) {
        taskId.value = _taskId
    }

    fun save(task: Task) = viewModelScope.launch(Dispatchers.IO) {
            taskRepository.updateTask(task)
    }

    fun delete(task: Task) = viewModelScope.launch(Dispatchers.IO) {
        taskRepository.removeTask(task)
    }

}