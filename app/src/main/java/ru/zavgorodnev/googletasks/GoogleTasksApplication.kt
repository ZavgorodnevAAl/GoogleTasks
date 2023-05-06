package ru.zavgorodnev.googletasks

import android.app.Application
import ru.zavgorodnev.googletasks.data.task.InDatabaseTaskRepository

class GoogleTasksApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        InDatabaseTaskRepository.init(this)
    }
}