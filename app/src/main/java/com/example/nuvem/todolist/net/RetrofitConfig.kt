package com.example.nuvem.todolist.net

import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
class RetrofitConfig {

    val retrofit = Retrofit
        .Builder()
        .baseUrl("http://34.66.174.25/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    // Serviço de listas
    @Provides
    fun listasService() = retrofit.create(ListasService::class.java)

    // Serviço de tarefas
    @Provides
    fun tarefasService() = retrofit.create(TarefasService::class.java)

}