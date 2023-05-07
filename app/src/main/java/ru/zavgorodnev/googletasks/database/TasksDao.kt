package ru.zavgorodnev.googletasks.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import ru.zavgorodnev.googletasks.data.task.Task
import java.util.UUID

@Dao
interface TaskDao {

    @Query("SELECT * FROM task WHERE isCompleted = 0 AND parent IS NULL")
    fun getTasks(): LiveData<List<Task>>

    @Query("SELECT * FROM task WHERE isFavorite = 1 AND isCompleted = 0 AND parent IS NULL")
    fun getFavoriteTasks(): LiveData<List<Task>>

    @Query("SELECT * FROM task WHERE isCompleted = 1 AND parent IS NULL")
    fun getCompletedTasks(): LiveData<List<Task>>

    @Query("SELECT * FROM task WHERE parent=(:parentId)")
    fun getSubtasks(parentId: UUID): LiveData<List<Task>>

    @Query("SELECT * FROM task WHERE id=(:id)")
    fun getTask(id: UUID): LiveData<Task?>

    @Query("UPDATE task SET isCompleted = 1 WHERE parent=(:parentId)")
    fun markSubtasksAsCompleted(parentId: UUID)

    @Query("DELETE FROM task WHERE parent=(:parentId)")
    fun deleteSubtasks(parentId: UUID)

    @Insert
    suspend fun addTask(task: Task)

    @Update
    suspend fun updateTask(task: Task)

    @Delete
    suspend fun deleteTask(task: Task)
}