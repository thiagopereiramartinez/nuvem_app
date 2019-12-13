package com.example.nuvem.todolist.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.nuvem.todolist.dagger.DaggerViewModelComponent
import com.example.nuvem.todolist.models.ListaModel
import com.example.nuvem.todolist.models.ResponseModel
import com.example.nuvem.todolist.models.TarefaModel
import com.example.nuvem.todolist.net.TarefasService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class TarefaViewModel : ViewModel() {

    @Inject
    lateinit var tarefasService: TarefasService

    init {
        DaggerViewModelComponent.builder().build().inject(this)
    }

    var tarefas: MutableLiveData<List<TarefaModel>>? = null

    fun getDados(idlista: String) {
        this.tarefas = MutableLiveData<List<TarefaModel>>().apply {
            loadListas(idlista, this)
        }
    }

    // Carregar listas
    fun loadListas(idlista: String, list: MutableLiveData<List<TarefaModel>>) {
        tarefasService.all(idlista).enqueue(object: Callback<List<TarefaModel>> {

            override fun onFailure(call: Call<List<TarefaModel>>, t: Throwable) {
                println(t)
            }

            override fun onResponse(
                call: Call<List<TarefaModel>>,
                response: Response<List<TarefaModel>>
            ) {
                list.value = response.body()
            }
        })
    }

}