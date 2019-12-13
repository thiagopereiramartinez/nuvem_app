package com.example.nuvem.todolist.net

import com.example.nuvem.todolist.models.TarefaModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface TarefasService {

    // Obter todas as listas
    @GET("listas/{idlista}/tarefas")
    fun all(@Path("idlista") idlista: String): Call<List<TarefaModel>>

}