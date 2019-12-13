package com.example.nuvem.todolist.adapters

import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.nuvem.todolist.TarefasFragmentDirections
import com.example.nuvem.todolist.R
import com.example.nuvem.todolist.models.ListaModel
import com.example.nuvem.todolist.models.TarefaModel
import android.graphics.Paint.STRIKE_THRU_TEXT_FLAG
import android.graphics.Paint

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

        // Tarefa marcada como concluida
        if (listTarefas.get(position).status == TarefaModel.Status.DONE.status) {
            holder.titulo.setPaintFlags(holder.titulo.getPaintFlags() or Paint.STRIKE_THRU_TEXT_FLAG)
            holder.titulo.setTypeface(holder.titulo.typeface, Typeface.ITALIC)
            holder.checkBox.isChecked = true
        } else {
            // Tarefa ainda não foi concluída
            holder.titulo.setPaintFlags(holder.titulo.getPaintFlags() and Paint.STRIKE_THRU_TEXT_FLAG.inv())
            holder.checkBox.isChecked = false
            holder.titulo.typeface = Typeface.DEFAULT
        }

        // Alterar status quando clicar no Checkbox
        holder.checkBox.setOnClickListener {
            listTarefas.get(position).status = if (holder.checkBox.isChecked) TarefaModel.Status.DONE.status else TarefaModel.Status.PENDING.status
            notifyDataSetChanged()
        }

        // Editar tarefa
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