package com.example.nuvem.todolist.net

import com.example.nuvem.todolist.models.ListaModel
import dagger.Module
import dagger.Provides
import retrofit2.Call
import retrofit2.http.*

interface ListasService {

    // Obter todas as listas
    @GET("listas")
    fun all(): Call<List<ListaModel>>

    // Obter lista em específico
    @GET("listas/{id}")
    fun lista(@Path("id") id: Int): Call<ListaModel>

    // Inserir lista
    @POST("listas")
    fun inserir(@Body lista: ListaModel)

    // Alterar lista
    @PUT("listas/{id}")
    fun alterar(@Path("id") id: Int, @Body lista: ListaModel)

    // Apagar lista
    @DELETE("listas/{id}")
    fun excluir(@Path("id") id: Int)

}
