package com.example.nuvem.todolist


import android.app.AlertDialog
import android.opengl.Visibility
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.nuvem.todolist.adapters.ListasAdapter
import com.example.nuvem.todolist.extensions.snackbar
import com.example.nuvem.todolist.models.ListaModel
import com.example.nuvem.todolist.net.ListasService
import com.example.nuvem.todolist.viewmodels.ListaViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.button.MaterialButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.*
import javax.inject.Inject

class ListasFragment : Fragment() {

    // Variáveis
    private lateinit var emptyState: TextView
    private lateinit var swipeToRefresh: SwipeRefreshLayout
    private lateinit var btnNovo: FloatingActionButton
    private lateinit var recyclerView: RecyclerView
    private val adapter: ListasAdapter by lazy {
        ListasAdapter(this, arrayListOf())
    }
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
        emptyState.text = "Nenhuma lista"
        emptyState.visibility = View.GONE

        // Configurar ViewModel
        model.listas.observe(this, Observer {
            adapter.insertAll(it)
            swipeToRefresh.isRefreshing = false

            // Controlar visibilidade da mensagem de nenhuma lista
            emptyState.visibility = if (adapter.list.isEmpty()) View.VISIBLE else View.GONE

        })

        // Configurar RecyclerView
        setupRecyclerView()

        // Botão novo
        btnNovo.setOnClickListener {
            val sheetDialog = BottomSheetDialog(this.context!!)
            val sheetView = activity!!.layoutInflater.inflate(R.layout.bottom_sheet_nova_lista, null)

            val listaText = (sheetView.findViewById(R.id.listaEdit) as EditText).apply {
                it.requestFocus()
            }

            // Botão confirmar
            val btnConfirmar: Button = sheetView.findViewById(R.id.btnSalvar)
            btnConfirmar.setOnClickListener {
                model.inserirLista(ListaModel(
                    id = UUID.randomUUID().toString(),
                    nome = listaText.text.toString()))
                sheetDialog.dismiss()
            }

            // Abrir dialog
            sheetDialog.apply {
                setContentView(sheetView)
                setOnShowListener {
                    listaText.requestFocus()
                    sheetDialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
                }
                show()
            }

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

        swipeToRefresh.isRefreshing = true
        swipeToRefresh.setOnRefreshListener {
            model.loadListas(model.listas)
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
            .setTitle("Editar lista")
            .setView(view)
            .setPositiveButton("Salvar") { _, _ ->
                lista.nome = editText.text.toString()

                // Fazer edição
                model.editarLista(lista)
                adapter.notifyItemChanged(position)
            }
            .setNegativeButton("Cancelar") { dialogInterface, _ ->
                dialogInterface.dismiss()
            }
            .create()

        alert.setOnShowListener {
            alert.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
        }

        alert.show()
    }

    // Excluir
    fun excluir(lista: ListaModel) {
        AlertDialog
            .Builder(context)
            .setMessage("Deseja excluir esta lista ?")
            .setPositiveButton("Sim") { _, _ ->
                model.excluirLista(lista)

                "Lista excluída".snackbar(view!!) {
                    model.inserirLista(lista)
                }
            }
            .setNegativeButton("Não") { dialogInterface, _ ->
                dialogInterface.dismiss()
            }
            .show()
    }

    // Atualizar o título da AppBar
    override fun onResume() {
        super.onResume()

        activity?.title = "Nuvem To-Do List"
    }

}
