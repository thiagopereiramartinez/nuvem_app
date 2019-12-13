package com.example.nuvem.todolist


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.nuvem.todolist.adapters.TarefasAdapter
import com.example.nuvem.todolist.viewmodels.TarefaViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.floatingactionbutton.FloatingActionButton

class TarefasFragment : Fragment() {

    // Variáveis
    private lateinit var swipeToRefresh: SwipeRefreshLayout
    private lateinit var btnNovo: FloatingActionButton
    private lateinit var recyclerView: RecyclerView
    private val adapter: TarefasAdapter by lazy {
        TarefasAdapter(this, arrayListOf())
    }
    private val model: TarefaViewModel by lazy {
        ViewModelProviders.of(this)[TarefaViewModel::class.java]
    }

    // onCreateView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_lista_tarefas, container, false)

        // Configurar ViewModel
        model.getDados("5df28329886fe20010d78a44")
        model.tarefas?.observe(this, Observer {
            adapter.insertAll(it)
            swipeToRefresh.isRefreshing = false
        })

        // Bind components
        swipeToRefresh = view.findViewById(R.id.swipeToRefresh)
        recyclerView = view.findViewById(R.id.recyclerView)
        btnNovo = view.findViewById(R.id.btnNovo)

        // Setup RecyclerView
        setupRecyclerView()

        // Botão Novo
        btnNovo.setOnClickListener {
            val sheetDialog = BottomSheetDialog(this.context!!)
            val sheetView = activity!!.layoutInflater.inflate(R.layout.bottom_sheet_nova_tarefa, null)

            sheetDialog.apply {
                setContentView(sheetView)
                show()
            }

        }

        return view
    }

    // Setup RecyclerView
    private fun setupRecyclerView() {
        recyclerView.apply {
            layoutManager = LinearLayoutManager(this.context)
            adapter = this@TarefasFragment.adapter
            addItemDecoration(
                DividerItemDecoration(
                    this@TarefasFragment.context,
                    DividerItemDecoration.VERTICAL
                )
            )
        }

        swipeToRefresh.isRefreshing = true
        swipeToRefresh.setOnRefreshListener {
            model.loadListas("5df28329886fe20010d78a44", model.tarefas!!)
        }
    }

}
