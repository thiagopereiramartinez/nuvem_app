package com.example.nuvem.todolist.models

import androidx.room.*

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = ListaModel::class,
            parentColumns = [ "id" ],
            childColumns = [ "idlista" ]
        )
    ]
)
data class TarefaModel(
    @PrimaryKey val id: String,
    @ColumnInfo val idlista: String,
    @ColumnInfo val tarefa: String,
    @ColumnInfo val created_at: String,
    @ColumnInfo val status: String
)
