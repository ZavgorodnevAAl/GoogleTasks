package ru.zavgorodnev.googletasks.ui.detail

import ru.zavgorodnev.googletasks.data.task.Task
import ru.zavgorodnev.googletasks.ui.list.TaskItemListener

interface SubtaskItemListener : TaskItemListener {

    fun deleteSubtask(subtask: Task)
}