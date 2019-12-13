package com.example.nuvem.todolist.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.nuvem.todolist.ListasFragmentDirections
import com.example.nuvem.todolist.R
import com.example.nuvem.todolist.TarefasFragmentDirections
import com.example.nuvem.todolist.models.ListaModel

class ListasAdapter(
    private val fragment: Fragment,
    private val list: ArrayList<ListaModel>
) : RecyclerView.Adapter<ListasAdapter.ListasViewHolder>() {

    // ViewHolder
    class ListasViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val titulo: TextView
        val totalTarefas: TextView

        init {
            titulo = itemView.findViewById(R.id.titulo)
            totalTarefas = itemView.findViewById(R.id.totalTarefas)
        }

    }

    // onCreateViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ListasViewHolder(LayoutInflater.from(fragment.context).inflate(R.layout.item_lista_listas, parent, false))

    // getItemCount
    override fun getItemCount() = list.size

    // onBindViewHolder
    override fun onBindViewHolder(holder: ListasViewHolder, position: Int) {
        holder.itemView.setOnClickListener {
            val action = ListasFragmentDirections.actionListaListasFragmentToListaTarefasFragment(list.get(position))
            fragment.findNavController().navigate(action)
        }

        holder.titulo.text = list.get(position).nome
    }

    // insertAll
    fun insertAll(list: List<ListaModel>) {
        this.list.clear()
        this.list.addAll(list)
        notifyDataSetChanged()
    }

}
