package com.example.nuvem.todolist.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.nuvem.todolist.TarefasFragmentDirections
import com.example.nuvem.todolist.R
import com.example.nuvem.todolist.models.ListaModel
import com.example.nuvem.todolist.models.TarefaModel

class TarefasAdapter(
    private val fragment: Fragment,
    private val listTarefas: ArrayList<TarefaModel>
    ) : RecyclerView.Adapter<TarefasAdapter.TarefasViewHolder>() {

    // ViewHolder
    class TarefasViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        val checkBox: CheckBox
        val titulo: TextView

        init {
            this.checkBox = itemView.findViewById(R.id.checkBox)
            this.titulo = itemView.findViewById(R.id.titulo)
        }

    }

    // onCreateViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TarefasViewHolder =
        TarefasViewHolder(LayoutInflater.from(fragment.context).inflate(R.layout.item_lista_tarefas, parent, false))

    // getItemCount
    override fun getItemCount(): Int = listTarefas.size

    // onBindViewHolder
    override fun onBindViewHolder(holder: TarefasViewHolder, position: Int) {
        holder.titulo.text = listTarefas.get(position).tarefa
        holder.itemView.setOnClickListener {
            val action = TarefasFragmentDirections.actionListaTarefasFragmentToDetalhesTarefaFragment()
            fragment.findNavController().navigate(action)
        }
    }

    // insertAll
    fun insertAll(list: List<TarefaModel>) {
        this.listTarefas.clear()
        this.listTarefas.addAll(list)
        notifyDataSetChanged()
    }

}