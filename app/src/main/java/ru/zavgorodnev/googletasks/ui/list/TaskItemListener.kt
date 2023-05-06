package ru.zavgorodnev.googletasks.ui.list

import ru.zavgorodnev.googletasks.data.task.Task
import java.util.UUID

interface TaskItemListener {

    fun onClickTask(taskId: UUID)

    fun onFavoriteButtonPressed(task: Task)

    fun onCompletedButtonPressed(task: Task)
}