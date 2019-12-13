package com.example.nuvem.todolist.viewmodels

import android.app.Application
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.nuvem.todolist.dagger.DaggerViewModelComponent
import com.example.nuvem.todolist.db.AppDatabase
import com.example.nuvem.todolist.models.ListaModel
import com.example.nuvem.todolist.models.ResponseModel
import com.example.nuvem.todolist.net.ListasService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import javax.inject.Inject

class ListaViewModel(application: Application) : AndroidViewModel(application) {

    // Serviço do Retrofit
    @Inject
    lateinit var listasService: ListasService

    // Banco de dados Room
    private val db: AppDatabase by lazy {
        Room.databaseBuilder(application.applicationContext, AppDatabase::class.java, "nuvem").build()
    }

    // Injetar dependências
    init {
        DaggerViewModelComponent.builder().build().inject(this)
    }

    // MutableLiveData
    val listas: MutableLiveData<List<ListaModel>> by lazy {
        MutableLiveData<List<ListaModel>>().apply {
            loadListas(this)
        }
    }

    // Carregar listas
    fun loadListas(list: MutableLiveData<List<ListaModel>>) {
        listasService.all().enqueue(object: Callback<List<ListaModel>> {

            // Se houver falha, ler os dados que estão salvos no Room
            override fun onFailure(call: Call<List<ListaModel>>, t: Throwable) {
                // Ler os dados do Room
                Executors.newSingleThreadExecutor().execute {
                    val list = db.listasDao().getAll()

                    // Executar na MainThread
                    ContextCompat.getMainExecutor(this@ListaViewModel.getApplication() as Application).execute {
                        this@ListaViewModel.listas.value = list
                    }
                }
            }

            override fun onResponse(
                call: Call<List<ListaModel>>,
                response: Response<List<ListaModel>>
            ) {
                // Adicionar resultado ao LiveData
                list.value = response.body()?.sortedBy { it.nome }

                // Apagar todos os registros no banco
                Executors.newSingleThreadExecutor().execute {
                    db.listasDao().deleteAll()
                }

                // Adicionar no banco de dados
                response.body()?.forEach {
                    Executors.newSingleThreadExecutor().execute {
                        db.listasDao().insert(it)
                    }
                }
            }
        })
    }

    // Inserir lista
    fun inserirLista(listModel: ListaModel) {
        listas.value = listas.value?.plus(listModel)?.sortedBy { it.nome }

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
