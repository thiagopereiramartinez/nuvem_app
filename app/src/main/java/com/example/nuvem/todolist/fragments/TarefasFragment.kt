package com.example.nuvem.todolist.fragments


import android.app.AlertDialog
import android.os.Bundle
import android.util.DisplayMetrics
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.nuvem.todolist.R
import com.example.nuvem.todolist.adapters.TarefasAdapter
import com.example.nuvem.todolist.extensions.snackbar
import com.example.nuvem.todolist.models.TarefaModel
import com.example.nuvem.todolist.viewmodels.TarefaViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.*

class TarefasFragment : Fragment() {

    // Variáveis
    private lateinit var emptyState: TextView
    private lateinit var swipeToRefresh: SwipeRefreshLayout
    private lateinit var btnNovo: FloatingActionButton
    private lateinit var recyclerView: RecyclerView
    private val adapter: TarefasAdapter by lazy {
        TarefasAdapter(this, arrayListOf())
    }

    // ViewModel
    private val model: TarefaViewModel by lazy {
        ViewModelProviders.of(this)[TarefaViewModel::class.java]
    }

    // Argumentos recebidos pelo fragment anterior
    val args: TarefasFragmentArgs by navArgs()

    // onCreateView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_lista_tarefas, container, false)

        // Setar o titulo do Toolbar com o nome da lista
        activity?.title = args.lista.nome

        // Adicionar observador ao ViewModel
        model.getDados(args.lista.id)
        model.tarefas?.observe(this, Observer {
            adapter.insertAll(it)
            swipeToRefresh.isRefreshing = false

            // Controlar visibilidade da mensagem de nenhuma lista
            emptyState.visibility = if (adapter.listTarefas.isEmpty()) View.VISIBLE else View.GONE

        })

        // Bindar componentes
        swipeToRefresh = view.findViewById(R.id.swipeToRefresh)
        recyclerView = view.findViewById(R.id.recyclerView)
        btnNovo = view.findViewById(R.id.btnNovo)
        emptyState = view.findViewById(R.id.emptyState)
        emptyState.text = "Nenhuma tarefa"
        emptyState.visibility = View.GONE

        // Configurar RecyclerView
        setupRecyclerView()

        // Botão novo
        btnNovo.setOnClickListener {
            incluir()
        }

        return view
    }

    // Configurar RecyclerView
    private fun setupRecyclerView() {
        recyclerView.apply {
            layoutManager = LinearLayoutManager(this.context)
            adapter = this@TarefasFragment.adapter
            setHasFixedSize(true)
            addItemDecoration(
                DividerItemDecoration(
                    this@TarefasFragment.context,
                    DividerItemDecoration.VERTICAL
                )
            )
        }

        // Configurar o Swipe To Refresh
        swipeToRefresh.isRefreshing = true
        swipeToRefresh.setOnRefreshListener {
            model.loadTarefas(args.lista.id, model.tarefas!!)
        }
    }

    // Incluir
    private fun incluir() {
        val sheetDialog = BottomSheetDialog(this.context!!)
        val sheetView = activity!!.layoutInflater.inflate(R.layout.bottom_sheet_nova_tarefa, null)

        val tarefaText = (sheetView.findViewById(R.id.tarefaEdit) as EditText).apply {
            requestFocus()
        }

        // Botão confirmar
        val btnConfirmar: Button = sheetView.findViewById(R.id.btnSalvar)
        btnConfirmar.setOnClickListener {
            if (tarefaText.text.toString().trim().length == 0) {
                sheetDialog.dismiss()
                return@setOnClickListener
            }

            model.inserirTarefa(args.lista.id, TarefaModel(
                id = UUID.randomUUID().toString(),
                idlista = args.lista.id,
                tarefa = tarefaText.text.toString()
            ))
            sheetDialog.dismiss()
        }

        // Abrir dialog
        sheetDialog.apply {
            setContentView(sheetView)
            setOnShowListener {
                // Abrir o teclado quando mostrar o alert
                sheetDialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)

                // Corrigir sobreposição do teclado
                val childLayout = sheetView.layoutParams
                val displayMetrics = DisplayMetrics()

                requireActivity()
                    .windowManager
                    .defaultDisplay
                    .getMetrics(displayMetrics)

                childLayout.height = displayMetrics.heightPixels / 2
                sheetView.layoutParams = childLayout
            }
            show()
        }
    }

    // Editar
    fun editar(tarefa: TarefaModel, position: Int) {
        // Inflar a caixa de texto
        val view = LayoutInflater.from(context).inflate(R.layout.edit_dialog, null )
        val editText: EditText = view.findViewById(R.id.editText)
        editText.setText(tarefa.tarefa)
        editText.requestFocus()

        // Abrir dialog
        val alert = AlertDialog
            .Builder(context)
            .setTitle("Editar tarefa")
            .setView(view)
            .setPositiveButton("Salvar") { dialogInterface, _ ->
                if (editText.text.toString().isBlank()) {
                    dialogInterface.dismiss()
                    return@setPositiveButton
                }

                tarefa.tarefa = editText.text.toString()

                // Fazer edição
                model.editarTarefa(tarefa)
                adapter.notifyItemChanged(position)
            }
            .setNegativeButton("Cancelar") { dialogInterface, _ ->
                dialogInterface.dismiss()
            }
            .create()

        // Abrir o teclado quando abrir o alert
        alert.setOnShowListener {
            alert.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
        }

        alert.show()
    }

    // Excluir
    fun excluir(tarefa: TarefaModel) {
        AlertDialog
            .Builder(context)
            .setMessage("Deseja excluir esta tarefa ?")
            .setPositiveButton("Sim") { _, _ ->
                model.excluirTarefa(tarefa)

                "Tarefa excluída".snackbar(view!!) {
                    model.inserirTarefa(args.lista.id, tarefa)
                }
            }
            .setNegativeButton("Não") { dialogInterface, _ ->
                dialogInterface.dismiss()
            }
            .show()
    }

    // Marcar/desmarcar tarefa
    fun marcarDesmarcar(tarefa: TarefaModel) {
        model.marcarDesmarcar(tarefa)
    }

}
