package com.example.nuvem.todolist.utils

import android.view.View
import com.google.android.material.snackbar.Snackbar
import com.example.nuvem.todolist.R

// Exibir Snackbar
fun String.snackbar(view: View, duration: Int = Snackbar.LENGTH_LONG, actionLabel: String = view.resources.getString(R.string.desfazer), action: () -> Unit) {
    Snackbar.make(view, this, duration).setAction(actionLabel) {
        action()
    }.show()
}
