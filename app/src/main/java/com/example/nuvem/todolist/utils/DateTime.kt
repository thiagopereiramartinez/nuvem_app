package com.example.nuvem.todolist.utils

import java.text.SimpleDateFormat
import java.util.*

class DateTime {

    companion object {

        val now: String
            get() = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault()).format(Date())

    }

}