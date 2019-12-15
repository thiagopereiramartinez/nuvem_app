package com.example.nuvem.todolist.views.adapters

import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.nuvem.todolist.R
import com.example.nuvem.todolist.models.TarefaModel
import android.graphics.Paint
import android.widget.*
import com.example.nuvem.todolist.views.TarefasFragment

class TarefasAdapter(
    private val fragment: TarefasFragment,
    var listTarefas: List<TarefaModel>
    ) : RecyclerView.Adapter<TarefasAdapter.TarefasViewHolder>() {

    // ViewHolder
    class TarefasViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        val checkBox: CheckBox
        val titulo: TextView
        val btnMore: ImageButton

        init {
            this.checkBox = itemView.findViewById(R.id.checkBox)
            this.titulo = itemView.findViewById(R.id.titulo)
            this.btnMore = itemView.findViewById(R.id.btnMore)
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

        // Tarefa marcada como concluida
        if (listTarefas.get(position).completed) {
            holder.titulo.setPaintFlags(holder.titulo.getPaintFlags() or Paint.STRIKE_THRU_TEXT_FLAG)
            holder.titulo.setTypeface(holder.titulo.typeface, Typeface.ITALIC)
            holder.checkBox.isChecked = true
        } else {
            // Tarefa ainda não foi concluída
            holder.titulo.setPaintFlags(holder.titulo.getPaintFlags() and Paint.STRIKE_THRU_TEXT_FLAG.inv())
            holder.checkBox.isChecked = false
            holder.titulo.typeface = Typeface.DEFAULT
        }

        // Marcar/desmarcar como concluída
        holder.itemView.setOnClickListener {
            listTarefas.get(position).completed = listTarefas.get(position).completed.not()
            fragment.marcarDesmarcar(listTarefas.get(position))
            notifyItemChanged(position)
        }

        // Menu de opções
        holder.btnMore.setOnClickListener {
            val popup = PopupMenu(fragment.context!!, holder.btnMore)
            popup.apply {
                menuInflater.inflate(R.menu.popup_menu, menu)
                setOnMenuItemClickListener {
                    when (it.itemId) {
                        // Editar
                        R.id.menuEditar -> {
                            fragment.editar(listTarefas.get(position), position)
                            true
                        }
                        // Excluir
                        R.id.menuExcluir -> {
                            fragment.excluir(listTarefas.get(position))
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
    fun changeList(list: List<TarefaModel>) {
        this.listTarefas = list
        notifyDataSetChanged()
    }

}