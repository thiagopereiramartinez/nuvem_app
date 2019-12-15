package com.example.nuvem.todolist.views.adapters

import android.view.*
import android.widget.*
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.nuvem.todolist.views.ListasFragment
import com.example.nuvem.todolist.R
import com.example.nuvem.todolist.views.ListasFragmentDirections
import com.example.nuvem.todolist.models.ListaModel

class ListasAdapter(
    private val fragment: ListasFragment,
    var list: List<ListaModel>
) : RecyclerView.Adapter<ListasAdapter.ListasViewHolder>() {

    // ViewHolder
    class ListasViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val titulo: TextView = itemView.findViewById(R.id.titulo)
        val btnMore: ImageButton = itemView.findViewById(R.id.btnMore)

    }

    // onCreateViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ListasViewHolder(LayoutInflater.from(fragment.context).inflate(R.layout.item_lista_listas, parent, false))

    // getItemCount
    override fun getItemCount() = list.size

    // onBindViewHolder
    override fun onBindViewHolder(holder: ListasViewHolder, position: Int) {

        // Ao clicar sobre uma lista, irá para a tela de tarefas
        holder.itemView.setOnClickListener {
            val action = ListasFragmentDirections.actionListaListasFragmentToListaTarefasFragment(list[position])
            fragment.findNavController().navigate(action)
        }

        // Título da lista
        holder.titulo.text = list[position].nome

        // Menu de opções
        holder.btnMore.setOnClickListener {
            val popup = PopupMenu(fragment.context!!, holder.btnMore)
            popup.apply {
                menuInflater.inflate(R.menu.popup_menu, menu)
                setOnMenuItemClickListener {
                    when (it.itemId) {
                        // Editar
                        R.id.menuEditar -> {
                            fragment.editar(list[position], position)
                            true
                        }
                        // Excluir
                        R.id.menuExcluir -> {
                            fragment.excluir(list[position])
                            true
                        }
                        else -> false
                    }
                }
                show()
            }
        }
    }

    // Alterar lista
    fun changeList(list: List<ListaModel>) {
        this.list = list
        notifyDataSetChanged()
    }

}
