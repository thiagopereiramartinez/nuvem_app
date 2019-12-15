package com.example.nuvem.todolist.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.room.Room
import com.example.nuvem.todolist.dagger.DaggerViewModelComponent
import com.example.nuvem.todolist.db.AppDatabase
import com.example.nuvem.todolist.models.ListaModel
import com.example.nuvem.todolist.models.ResponseModel
import com.example.nuvem.todolist.net.ListasService
import com.example.nuvem.todolist.utils.Executor.Companion.backgroundThread
import com.example.nuvem.todolist.utils.Executor.Companion.mainThread
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
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
                backgroundThread {
                    val lista = db.listasDao().getAll()

                    // Executar na MainThread
                    mainThread(this@ListaViewModel.getApplication() as Application) {
                        this@ListaViewModel.listas.value = lista
                    }
                }
            }

            override fun onResponse(
                call: Call<List<ListaModel>>,
                response: Response<List<ListaModel>>
            ) {
                // Adicionar resultado ao LiveData
                list.value = response.body()?.sortedBy { it.nome }

                // Apagar todos as listas do banco
                response.body()?.run {
                    if (this.isNotEmpty()) {
                        backgroundThread {
                            db.listasDao().deleteAll()
                        }
                    }
                }

                // Adicionar no banco de dados
                response.body()?.forEach {
                    backgroundThread {
                        db.listasDao().insert(it)
                    }
                }
            }
        })
    }

    // Inserir lista
    fun inserirLista(listModel: ListaModel) {
        listas.value = listas.value?.plus(listModel)?.sortedBy { it.nome }

        // Inserir na API
        listasService.inserir(listModel).enqueue(object: Callback<ResponseModel> {
            override fun onFailure(call: Call<ResponseModel>, t: Throwable) = Unit

            override fun onResponse(
                call: Call<ResponseModel>,
                response: Response<ResponseModel>
            ) {

                // Atualizar o ID retornado pela API
                val res = response.body()
                res?.run {

                    val target = listas.value?.first { it.id == listModel.id }
                    target?.run {
                        target.id = res.message

                        // Excluir o ID antigo e incluir o novo
                        backgroundThread {
                            db.listasDao().delete(listModel)
                            db.listasDao().insert(target)
                        }
                    }
                }
            }
        })

        // Inserir no banco de dados
        backgroundThread {
            db.listasDao().insert(listModel)
        }
    }

    // Editar lista
    fun editarLista(listModel: ListaModel) {
        listas.value?.first { it.id == listModel.id }?.apply {
            nome = listModel.nome

            // Editar na API
            listasService.alterar(listModel.id, listModel).enqueue(object: Callback<ResponseModel> {
                override fun onFailure(call: Call<ResponseModel>, t: Throwable) = Unit

                override fun onResponse(
                    call: Call<ResponseModel>,
                    response: Response<ResponseModel>
                ) = Unit
            })

            // Editar no banco de dados
            backgroundThread {
                db.listasDao().insert(this)
            }
        }
    }

    // Excluir lista
    fun excluirLista(listModel: ListaModel) {
        listas.value = listas.value?.minus(listModel)?.sortedBy { it.nome }

        // Excluir na API
        listasService.excluir(listModel.id).enqueue(object: Callback<ResponseModel> {
            override fun onFailure(call: Call<ResponseModel>, t: Throwable) = Unit

            override fun onResponse(
                call: Call<ResponseModel>,
                response: Response<ResponseModel>
            ) = Unit
        })

        // Remover do banco de dados
        backgroundThread {
            db.listasDao().delete(listModel)
        }

    }

}
