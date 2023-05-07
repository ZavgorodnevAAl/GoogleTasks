package ru.zavgorodnev.googletasks.data.task

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Room
import ru.zavgorodnev.googletasks.database.TaskDatabase
import java.util.UUID


private const val DATABASE_NAME = "tasks-database"

class InDatabaseTaskRepository private constructor(context: Context) {

    private val database: TaskDatabase = Room.databaseBuilder(
        context.applicationContext,
        TaskDatabase::class.java,
        DATABASE_NAME
    ).build()

    private val tasksDao = database.taskDao()


    fun getTask(id: UUID): LiveData<Task?> = tasksDao.getTask(id)
    fun getTasks(): LiveData<List<Task>> = tasksDao.getTasks()
    fun getCompletedTasks(): LiveData<List<Task>> = tasksDao.getCompletedTasks()
    fun getFavouriteTasks(): LiveData<List<Task>> = tasksDao.getFavoriteTasks()
    fun getSubtasks(parentId: UUID): LiveData<List<Task>> = tasksDao.getSubtasks(parentId)

    suspend fun updateTask(task: Task) {

        tasksDao.updateTask(task)
        if (task.isCompleted) {
            tasksDao.markSubtasksAsCompleted(task.id)
        }
    }

    suspend fun removeTask(task: Task) {

        tasksDao.deleteTask(task)
        tasksDao.deleteSubtasks(task.id)
    }

    suspend fun add(task: Task) {

        tasksDao.addTask(task)
    }


    companion object {

        private var INSTANCE: InDatabaseTaskRepository? = null

        fun get(): InDatabaseTaskRepository {
            return INSTANCE ?: throw IllegalStateException("InDatabaseTaskRepository must be initialize")
        }

        fun init(context: Context) {
            if (INSTANCE == null) {
                INSTANCE = InDatabaseTaskRepository(context)
            }
        }

    }

}