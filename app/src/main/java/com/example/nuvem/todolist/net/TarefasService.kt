package com.example.nuvem.todolist.net

import com.example.nuvem.todolist.models.ResponseModel
import com.example.nuvem.todolist.models.TarefaModel
import retrofit2.Call
import retrofit2.http.*

interface TarefasService {

    // Obter todas as listas
    @GET("listas/{idlista}/tarefas")
    fun all(@Path("idlista") idlista: String): Call<List<TarefaModel>>

    // Inserir tarefa
    @POST("listas/{idlista}/tarefas")
    fun inserir(@Path("idlista") idlista: String, @Body tarefa: TarefaModel) : Call<ResponseModel>

    // Alterar tarefa
    @PUT("listas/{idlista}/tarefas/{id}")
    fun alterar(@Path("idlista") idlista: String, @Path("id") id: String, @Body tarefa: TarefaModel) : Call<ResponseModel>

    // Alterar status da tarefa
    @PATCH("listas/{idlista}/tarefas/{id}/status")
    fun status(@Path("idlista") idlista: String, @Path("id") id: String, @Body status: Map<String, Boolean>) : Call<ResponseModel>

    // Excluir tarefa
    @DELETE("listas/{idlista}/tarefas/{idtarefa}")
    fun excluir(@Path("idlista") idlista: String, @Path("idtarefa") idtarefa: String) : Call<ResponseModel>

}