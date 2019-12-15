package com.example.nuvem.todolist.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.Room
import com.example.nuvem.todolist.dagger.DaggerViewModelComponent
import com.example.nuvem.todolist.db.AppDatabase
import com.example.nuvem.todolist.models.ResponseModel
import com.example.nuvem.todolist.models.TarefaModel
import com.example.nuvem.todolist.net.TarefasService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject
import com.example.nuvem.todolist.utils.Executor.Companion.async
import com.example.nuvem.todolist.utils.Executor.Companion.sync

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
            loadTarefas(idlista, this)
        }
    }

    // Carregar listas
    fun loadTarefas(idlista: String, list: MutableLiveData<List<TarefaModel>>) {
        tarefasService.all(idlista).enqueue(object: Callback<List<TarefaModel>> {

            // Se houver falha, ler os dados que estão salvos no Room
            override fun onFailure(call: Call<List<TarefaModel>>, t: Throwable) {
                // Ler os dados do Room
                async {
                    val list = db.tarefasDao().getAll(idlista)

                    // Executar na MainThread
                    sync(this@TarefaViewModel.getApplication() as Application) {
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
                    async {
                        db.tarefasDao().insert(it)
                    }
                }
            }
        })
    }

    // Inserir tarefa
    fun inserirTarefa(idlista: String, tarefaModel: TarefaModel) {

        tarefas?.value = tarefas?.value?.plus(tarefaModel)?.sortedBy { it.created_at }

        // Inserir na API
        tarefasService.inserir(idlista, tarefaModel).enqueue(object: Callback<ResponseModel> {
            override fun onFailure(call: Call<ResponseModel>, t: Throwable) = Unit

            override fun onResponse(
                call: Call<ResponseModel>,
                response: Response<ResponseModel>
            ) {
                // Atualizar o ID retornado pela API
                val res = response.body()
                res?.run {

                    val target = tarefas?.value?.filter { it.id == tarefaModel.id }?.first()
                    target?.run {
                        target.id = res.message

                        // Excluir o ID antigo e incluir o novo
                        async {
                            db.tarefasDao().delete(tarefaModel)
                            db.tarefasDao().insert(target)
                        }
                    }
                }
            }
        })

        // Inserir no banco de dados
        async {
            db.tarefasDao().insert(tarefaModel)
        }
    }

    // Editar tarefa
    fun editarTarefa(tarefaModel: TarefaModel) {
        tarefas?.value?.filter { it.id == tarefaModel.id }?.first()?.apply {
            tarefa = tarefaModel.tarefa

            // Editar na API
            tarefasService.alterar(tarefaModel.idlista, tarefaModel.id, tarefaModel).enqueue(object: Callback<ResponseModel> {
                override fun onFailure(call: Call<ResponseModel>, t: Throwable) = Unit

                override fun onResponse(
                    call: Call<ResponseModel>,
                    response: Response<ResponseModel>
                ) = Unit
            })

            // Editar no banco de dados
            async {
                db.tarefasDao().insert(this)
            }
        }
    }

    // Excluir tarefa
    fun excluirTarefa(tarefaModel: TarefaModel) {
        tarefas?.value = tarefas?.value?.minus(tarefaModel)?.sortedBy { it.created_at }

        // Excluir na API
        tarefasService.excluir(tarefaModel.idlista, tarefaModel.id).enqueue(object: Callback<ResponseModel> {
            override fun onFailure(call: Call<ResponseModel>, t: Throwable) = Unit

            override fun onResponse(
                call: Call<ResponseModel>,
                response: Response<ResponseModel>
            ) = Unit
        })

        // Remover do banco de dados
        async {
            db.tarefasDao().delete(tarefaModel)
        }
    }

    // Marcar/desmarcar tarefa
    fun marcarDesmarcar(tarefaModel: TarefaModel) {
        tarefas?.value?.filter { it.id == tarefaModel.id }?.first()?.apply {

            // Alterar status na API
            tarefasService.status(tarefaModel.idlista, tarefaModel.id, mapOf("completed" to tarefaModel.completed)).enqueue(object: Callback<ResponseModel> {
                override fun onFailure(call: Call<ResponseModel>, t: Throwable) = Unit

                override fun onResponse(
                    call: Call<ResponseModel>,
                    response: Response<ResponseModel>
                ) = Unit
            })

            // Editar no banco de dados
            async {
                db.tarefasDao().insert(this)
            }
        }
    }

}