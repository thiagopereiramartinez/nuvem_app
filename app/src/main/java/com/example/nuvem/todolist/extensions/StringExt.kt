package com.example.nuvem.todolist.extensions

import android.app.Activity
import android.content.Context
import android.view.View
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar

fun String.toast(context: Context?, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(context, this, duration).show()
}

fun String.snackbar(view: View, duration: Int = Snackbar.LENGTH_LONG, actionLabel: String = "Desfazer", action: () -> Unit) {
    Snackbar.make(view, this, duration).setAction("Desfazer") {
        action()
    }.show()
}
