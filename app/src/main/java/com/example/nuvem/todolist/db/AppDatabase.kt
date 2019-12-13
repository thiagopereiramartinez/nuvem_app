package com.example.nuvem.todolist.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.nuvem.todolist.models.ListaModel
import com.example.nuvem.todolist.models.TarefaModel

@Database(entities = [
    ListaModel::class,
    TarefaModel::class
], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun listasDao(): ListasDao

    abstract fun tarefasDao(): TarefasDao

}
