package com.example.nuvem.todolist.models

import androidx.annotation.Keep
import androidx.room.*
import com.example.nuvem.todolist.utils.DateTime
import java.io.Serializable

@Keep
@Entity(tableName = "tarefas")
data class TarefaModel(
    @PrimaryKey var id: String,
    @ColumnInfo val idlista: String,
    @ColumnInfo var tarefa: String,
    @ColumnInfo val created_at: String = DateTime.now,
    @ColumnInfo var completed: Boolean = false
) : Serializable
