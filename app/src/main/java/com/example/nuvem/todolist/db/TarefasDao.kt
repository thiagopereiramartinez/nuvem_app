package com.example.nuvem.todolist.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.nuvem.todolist.models.TarefaModel

@Dao
interface TarefasDao {

    @Query("SELECT * FROM tarefas WHERE idlista=:idlista ORDER BY status, created_at")
    fun getAll(idlista: String): List<TarefaModel>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg tarefa: TarefaModel)

}
