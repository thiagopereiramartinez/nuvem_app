package com.example.nuvem.todolist.models

import androidx.annotation.Keep
import androidx.room.*
import com.example.nuvem.todolist.utils.DateTime

@Keep
@Entity(tableName = "tarefas")
data class TarefaModel(
    @PrimaryKey val id: String,
    @ColumnInfo val idlista: String,
    @ColumnInfo val tarefa: String,
    @ColumnInfo val created_at: String = DateTime.now,
    @ColumnInfo var status: String = Status.PENDING.status
) {

    enum class Status(val status: String) {
        PENDING("0"), DONE("1")
    }

}
