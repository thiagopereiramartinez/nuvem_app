package com.example.nuvem.todolist


import android.graphics.Canvas
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.nuvem.todolist.recyclerview.ListaTarefasAdapter
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ListaTarefasFragment : Fragment() {

    // Variáveis
    private lateinit var btnNovo: FloatingActionButton
    private lateinit var recyclerView: RecyclerView
    private val adapter: ListaTarefasAdapter by lazy {
        ListaTarefasAdapter(this)
    }

    // onCreateView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_lista_tarefas, container, false)

        // Bind components
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
            adapter = this@ListaTarefasFragment.adapter
            addItemDecoration(DividerItemDecoration(this@ListaTarefasFragment.context, DividerItemDecoration.VERTICAL))
        }

    }


}
