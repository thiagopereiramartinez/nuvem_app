package com.example.nuvem.todolist.viewmodels

import android.app.Application
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.room.Room
import com.example.nuvem.todolist.dagger.DaggerViewModelComponent
import com.example.nuvem.todolist.db.AppDatabase
import com.example.nuvem.todolist.models.ListaModel
import com.example.nuvem.todolist.models.ResponseModel
import com.example.nuvem.todolist.models.TarefaModel
import com.example.nuvem.todolist.net.TarefasService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.Executors
import javax.inject.Inject

class TarefaViewModel(application: Application) : AndroidViewModel(application) {

    // Serviço do Retrofit
    @Inject
    lateinit var tarefasService: TarefasService

    // Banco de dados Room
    private val db: AppDatabase by lazy {
        Room.databaseBuilder(application.applicationContext, AppDatabase::class.java, "nuvem").build()
    }

    // Injetar dependências
    init {
        DaggerViewModelComponent.builder().build().inject(this)
    }

    // MutableLiveData
    var tarefas: MutableLiveData<List<TarefaModel>>? = null

    fun getDados(idlista: String) {
        this.tarefas = MutableLiveData<List<TarefaModel>>().apply {
            loadListas(idlista, this)
        }
    }

    // Carregar listas
    fun loadListas(idlista: String, list: MutableLiveData<List<TarefaModel>>) {
        tarefasService.all(idlista).enqueue(object: Callback<List<TarefaModel>> {

            // Se houver falha, ler os dados que estão salvos no Room
            override fun onFailure(call: Call<List<TarefaModel>>, t: Throwable) {
                // Ler os dados do Room
                Executors.newSingleThreadExecutor().execute {
                    val list = db.tarefasDao().getAll(idlista)

                    // Executar na MainThread
                    ContextCompat.getMainExecutor(this@TarefaViewModel.getApplication() as Application).execute {
                        this@TarefaViewModel.tarefas?.value = list
                    }
                }
            }

            override fun onResponse(
                call: Call<List<TarefaModel>>,
                response: Response<List<TarefaModel>>
            ) {
                // Adicionar resultado ao LiveData
                list.value = response.body() ?: listOf()

                // Adicionar no banco de dados
                response.body()?.forEach {
                    Executors.newSingleThreadExecutor().execute {
                        db.tarefasDao().insert(it)
                    }
                }
            }
        })
    }

}