package com.example.nuvem.todolist.models

import androidx.annotation.Keep
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Keep
@Entity(tableName = "listas")
data class ListaModel(
    @PrimaryKey val id: String,
    @ColumnInfo val nome: String
) : Serializable
