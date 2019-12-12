package com.example.nuvem.todolist.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.nuvem.todolist.ListaTarefasFragmentDirections
import com.example.nuvem.todolist.R

class ListaTarefasAdapter(private val fragment: Fragment) : RecyclerView.Adapter<ListaTarefasAdapter.ListaTarefasViewHolder>() {

    // ViewHolder
    class ListaTarefasViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        val checkBox: CheckBox
        val titulo: TextView

        init {
            this.checkBox = itemView.findViewById(R.id.checkBox)
            this.titulo = itemView.findViewById(R.id.titulo)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListaTarefasViewHolder =
        ListaTarefasViewHolder(LayoutInflater.from(fragment.context).inflate(R.layout.item_lista_tarefas, parent, false))

    override fun getItemCount(): Int = 20

    override fun onBindViewHolder(holder: ListaTarefasViewHolder, position: Int) {
        holder.titulo.text = "Linha ${position}"
        holder.itemView.setOnClickListener {
            val action = ListaTarefasFragmentDirections.actionListaTarefasFragmentToDetalhesTarefaFragment()
            fragment.findNavController().navigate(action)
        }
    }

}