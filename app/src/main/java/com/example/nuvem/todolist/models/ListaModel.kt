package com.example.nuvem.todolist.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ListaModel(
    @PrimaryKey val id: String,
    @ColumnInfo val nome: String
)
