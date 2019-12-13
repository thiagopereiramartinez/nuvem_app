package com.example.nuvem.todolist.db

import androidx.room.*
import com.example.nuvem.todolist.models.ListaModel

@Dao
interface ListasDao {

    @Query("SELECT * FROM listas ORDER BY nome")
    fun getAll(): List<ListaModel>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg lista: ListaModel)

    @Delete
    fun delete(lista: ListaModel)

    @Query("DELETE FROM listas")
    fun deleteAll()

}
