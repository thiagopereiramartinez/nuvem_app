package com.example.nuvem.todolist.adapters

import android.view.*
import android.widget.*
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.nuvem.todolist.ListasFragment
import com.example.nuvem.todolist.ListasFragmentDirections
import com.example.nuvem.todolist.R
import com.example.nuvem.todolist.models.ListaModel

class ListasAdapter(
    private val fragment: ListasFragment,
    var list: List<ListaModel>
) : RecyclerView.Adapter<ListasAdapter.ListasViewHolder>() {

    // ViewHolder
    class ListasViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val titulo: TextView
        val btnMore: ImageButton

        init {
            titulo = itemView.findViewById(R.id.titulo)
            btnMore = itemView.findViewById(R.id.btnMore)
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

        // Menu de opções
        holder.btnMore.setOnClickListener {
            val popup = PopupMenu(fragment.context!!, holder.btnMore)
            popup.apply {
                menuInflater.inflate(R.menu.popup_menu, menu)
                setOnMenuItemClickListener {
                    when (it.itemId) {
                        // Editar
                        R.id.menuEditar -> {
                            fragment.editar(list.get(position), position)
                            true
                        }
                        // Excluir
                        R.id.menuExcluir -> {
                            fragment.excluir(list.get(position))
                            true
                        }
                        else -> false
                    }
                }
                show()
            }
        }
    }

    // insertAll
    fun insertAll(list: List<ListaModel>) {
        this.list = list
        notifyDataSetChanged()
    }

}
