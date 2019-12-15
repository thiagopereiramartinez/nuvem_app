package com.example.nuvem.todolist.utils

import android.app.Application
import androidx.core.content.ContextCompat
import java.util.concurrent.Executors

class Executor {

    companion object {

        fun async(run: () -> Unit) {
            Executors.newSingleThreadExecutor().execute(run)
        }

        fun sync(application: Application, run: () -> Unit) {
            ContextCompat.getMainExecutor(application).execute(run)
        }

    }

}
