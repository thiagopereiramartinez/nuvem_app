package com.example.nuvem.todolist.views


import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import android.widget.Button
import androidx.fragment.app.Fragment
import android.widget.EditText
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.nuvem.todolist.R
import com.example.nuvem.todolist.views.adapters.ListasAdapter
import com.example.nuvem.todolist.utils.snackbar
import com.example.nuvem.todolist.models.ListaModel
import com.example.nuvem.todolist.viewmodels.ListaViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.*

class ListasFragment : Fragment() {

    // Variáveis
    private lateinit var emptyState: TextView
    private lateinit var swipeToRefresh: SwipeRefreshLayout
    private lateinit var btnNovo: FloatingActionButton
    private lateinit var recyclerView: RecyclerView
    private val adapter: ListasAdapter by lazy {
        ListasAdapter(this, arrayListOf())
    }

    // ViewModel
    private val model: ListaViewModel by lazy {
        ViewModelProviders.of(this)[ListaViewModel::class.java]
    }

    // onCreateView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_lista_listas, container, false)

        // Bindar componentes
        recyclerView = view.findViewById(R.id.recyclerView)
        btnNovo = view.findViewById(R.id.btnNovo)
        swipeToRefresh = view.findViewById(R.id.swipeToRefresh)
        emptyState = view.findViewById(R.id.emptyState)

        emptyState.text = resources.getString(R.string.nenhuma_lista)
        emptyState.visibility = View.GONE

        // Adicionar observador ao ViewModel
        model.listas.observe(this, Observer {
            adapter.changeList(it)
            swipeToRefresh.isRefreshing = false

            // Controlar visibilidade da mensagem de nenhuma lista
            emptyState.visibility = if (adapter.list.isEmpty()) View.VISIBLE else View.GONE

        })

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
            adapter = this@ListasFragment.adapter
            addItemDecoration(
                DividerItemDecoration(
                    this@ListasFragment.context,
                    DividerItemDecoration.VERTICAL
                )
            )
        }

        // Configurar o Swipe To Refresh
        swipeToRefresh.isRefreshing = true
        swipeToRefresh.setOnRefreshListener {
            model.loadListas(model.listas)
        }
    }

    // Incluir
    private fun incluir() {
        val sheetDialog = BottomSheetDialog(this.context!!)
        val sheetView = activity!!.layoutInflater.inflate(R.layout.bottom_sheet_nova_lista, null)

        val listaText = (sheetView.findViewById(R.id.listaEdit) as EditText).apply {
            requestFocus()
        }

        // Botão confirmar
        val btnConfirmar: Button = sheetView.findViewById(R.id.btnSalvar)
        btnConfirmar.setOnClickListener {
            if (listaText.text.toString().isBlank()) {
                sheetDialog.dismiss()
                return@setOnClickListener
            }

            model.inserirLista(ListaModel(
                id = UUID.randomUUID().toString(),
                nome = listaText.text.toString()))
            sheetDialog.dismiss()
        }

        // Abrir dialog
        sheetDialog.apply {
            setContentView(sheetView)
            setOnShowListener {

                // Abrir teclado quando mostrar o alert
                sheetDialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)

            }
            show()
        }
    }

    // Editar
    fun editar(lista: ListaModel, position: Int) {
        // Inflar a caixa de texto
        val view = LayoutInflater.from(context).inflate(R.layout.edit_dialog, null )
        val editText:EditText = view.findViewById(R.id.editText)
        editText.setText(lista.nome)
        editText.requestFocus()

        // Abrir dialog
        val alert = AlertDialog
            .Builder(context)
            .setTitle(resources.getString(R.string.editar_lista))
            .setView(view)
            .setPositiveButton(resources.getString(R.string.salvar)) { dialogInterface, _ ->
                if (editText.text.toString().isBlank()) {
                    dialogInterface.dismiss()
                    return@setPositiveButton
                }

                lista.nome = editText.text.toString()

                // Fazer edição
                model.editarLista(lista)
                adapter.notifyItemChanged(position)
            }
            .setNegativeButton(resources.getString(R.string.cancelar)) { dialogInterface, _ ->
                dialogInterface.dismiss()
            }
            .create()

        // Abrir o teclado quando o dialog for exibido
        alert.setOnShowListener {
            alert.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
        }

        alert.show()
    }

    // Excluir
    fun excluir(lista: ListaModel) {
        AlertDialog
            .Builder(context)
            .setMessage(resources.getString(R.string.deseja_excluir_esta_lista))
            .setPositiveButton(resources.getString(R.string.sim)) { _, _ ->
                model.excluirLista(lista)

                resources.getString(R.string.lista_excluida).snackbar(view!!) {
                    model.inserirLista(lista)
                }
            }
            .setNegativeButton(resources.getString(R.string.nao)) { dialogInterface, _ ->
                dialogInterface.dismiss()
            }
            .show()
    }

    // Atualizar o título da AppBar
    override fun onResume() {
        super.onResume()
        activity?.title = resources.getString(R.string.app_name)
    }

}
