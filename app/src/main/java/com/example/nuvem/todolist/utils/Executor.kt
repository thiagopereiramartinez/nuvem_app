package com.example.nuvem.todolist.utils

import android.app.Application
import androidx.core.content.ContextCompat
import java.util.concurrent.Executors

class Executor {

    companion object {

        // Background
        fun backgroundThread(run: () -> Unit) {
            Executors.newSingleThreadExecutor().execute(run)
        }

        // MainThread
        fun mainThread(application: Application, run: () -> Unit) {
            ContextCompat.getMainExecutor(application).execute(run)
        }

    }

}
