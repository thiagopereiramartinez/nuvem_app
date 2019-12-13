package com.example.nuvem.todolist


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.nuvem.todolist.adapters.ListasAdapter
import com.example.nuvem.todolist.models.ListaModel
import com.example.nuvem.todolist.net.ListasService
import com.example.nuvem.todolist.viewmodels.ListaViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.floatingactionbutton.FloatingActionButton
import javax.inject.Inject

class ListasFragment : Fragment() {

    // Variáveis
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

        // Configurar ViewModel
        model.listas.observe(this, Observer {
            adapter.insertAll(it)
            swipeToRefresh.isRefreshing = false
        })

        // Bindar componentes
        recyclerView = view.findViewById(R.id.recyclerView)
        btnNovo = view.findViewById(R.id.btnNovo)
        swipeToRefresh = view.findViewById(R.id.swipeToRefresh)

        // Configurar RecyclerView
        setupRecyclerView()

        // Botão novo
        btnNovo.setOnClickListener {
            val sheetDialog = BottomSheetDialog(this.context!!)
            val sheetView = activity!!.layoutInflater.inflate(R.layout.bottom_sheet_nova_lista, null)

            val listaText: EditText = sheetView.findViewById(R.id.listaEdit)
            val btnConfirmar: ImageButton = sheetView.findViewById(R.id.btnConfirmar)
            btnConfirmar.setOnClickListener {
                model.inserirLista(ListaModel("", listaText.text.toString()))
                sheetDialog.dismiss()
            }

            sheetDialog.apply {
                setContentView(sheetView)
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

}
