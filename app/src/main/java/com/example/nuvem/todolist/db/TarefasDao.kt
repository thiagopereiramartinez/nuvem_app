package com.example.nuvem.todolist.db

import androidx.room.*
import com.example.nuvem.todolist.models.TarefaModel

@Dao
interface TarefasDao {

    @Query("SELECT * FROM tarefas WHERE idlista=:idlista ORDER BY created_at")
    fun getAll(idlista: String): List<TarefaModel>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg tarefa: TarefaModel)

    @Delete
    fun delete(tarefa: TarefaModel)

}
