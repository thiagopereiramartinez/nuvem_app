package com.example.nuvem.todolist.viewmodels

import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.nuvem.todolist.dagger.DaggerViewModelComponent
import com.example.nuvem.todolist.models.ListaModel
import com.example.nuvem.todolist.models.ResponseModel
import com.example.nuvem.todolist.net.ListasService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class ListaViewModel : ViewModel() {

    @Inject
    lateinit var listasService: ListasService

    init {
        DaggerViewModelComponent.builder().build().inject(this)
    }

    val listas: MutableLiveData<List<ListaModel>> by lazy {
        MutableLiveData<List<ListaModel>>().apply {
            loadListas(this)
        }
    }

    // Carregar listas
    fun loadListas(list: MutableLiveData<List<ListaModel>>) {
        listasService.all().enqueue(object: Callback<List<ListaModel>> {

            override fun onFailure(call: Call<List<ListaModel>>, t: Throwable) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onResponse(
                call: Call<List<ListaModel>>,
                response: Response<List<ListaModel>>
            ) {
                list.value = response.body()
            }
        })
    }

    // Inserir lista
    fun inserirLista(listModel: ListaModel) {
        listas.value = listas.value?.plus(listModel)

        listasService.inserir(listModel).enqueue(object: Callback<ResponseModel> {
            override fun onFailure(call: Call<ResponseModel>, t: Throwable) {
                println(t)
            }

            override fun onResponse(call: Call<ResponseModel>, response: Response<ResponseModel>) {
                println("OK")
            }
        })
    }

}
