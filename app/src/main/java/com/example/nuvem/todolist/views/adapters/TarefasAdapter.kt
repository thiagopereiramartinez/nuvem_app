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

        val checkBox: CheckBox = itemView.findViewById(R.id.checkBox)
        val titulo: TextView = itemView.findViewById(R.id.titulo)
        val btnMore: ImageButton = itemView.findViewById(R.id.btnMore)

    }

    // onCreateViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TarefasViewHolder =
        TarefasViewHolder(LayoutInflater.from(fragment.context).inflate(R.layout.item_lista_tarefas, parent, false))

    // getItemCount
    override fun getItemCount(): Int = listTarefas.size

    // onBindViewHolder
    override fun onBindViewHolder(holder: TarefasViewHolder, position: Int) {

        // Titulo da tarefa
        holder.titulo.text = listTarefas[position].tarefa

        // Tarefa marcada como concluida
        if (listTarefas[position].completed) {
            holder.titulo.paintFlags = holder.titulo.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG // Riscar texto
            holder.titulo.setTypeface(holder.titulo.typeface, Typeface.ITALIC)
            holder.checkBox.isChecked = true
        } else {
            // Tarefa ainda não foi concluída
            holder.titulo.paintFlags = holder.titulo.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv() // Não riscar o texto
            holder.titulo.typeface = Typeface.DEFAULT
            holder.checkBox.isChecked = false
        }

        // Marcar/desmarcar como concluída
        holder.itemView.setOnClickListener {
            listTarefas[position].completed = listTarefas[position].completed.not()
            fragment.marcarDesmarcar(listTarefas[position])
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
                            fragment.editar(listTarefas[position], position)
                            true
                        }
                        // Excluir
                        R.id.menuExcluir -> {
                            fragment.excluir(listTarefas[position])
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