package com.example.nuvem.todolist.adapters

import android.app.AlertDialog
import android.content.Context
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.nuvem.todolist.ListasFragmentDirections
import com.example.nuvem.todolist.R
import com.example.nuvem.todolist.TarefasFragmentDirections
import com.example.nuvem.todolist.models.ListaModel
import com.example.nuvem.todolist.viewmodels.ListaViewModel

class ListasAdapter(
    private val fragment: Fragment,
    private val list: ArrayList<ListaModel>
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
                            editar(list.get(position), position)
                            true
                        }
                        // Excluir
                        R.id.menuExcluir -> {
                            excluir(list.get(position))
                            true
                        }
                        else -> false
                    }
                }
                show()
            }
        }
    }

    // Editar
    private fun editar(lista: ListaModel, position: Int) {
        // Inflar a caixa de texto
        val view = LayoutInflater.from(fragment.context).inflate(R.layout.edit_dialog, null )
        val editText:EditText = view.findViewById(R.id.editText)
        editText.setText(lista.nome)
        editText.requestFocus()

        // Abrir dialog
        val alert = AlertDialog
            .Builder(fragment.context)
            .setTitle("Editar lista")
            .setView(view)
            .setPositiveButton("Salvar") { _, _ ->
                lista.nome = editText.text.toString()

                // Fazer edição
                val model = ViewModelProviders.of(fragment)[ListaViewModel::class.java]
                model.editarLista(lista)
                notifyItemChanged(position)
            }
            .setNegativeButton("Cancelar") { dialogInterface, _ ->
                dialogInterface.dismiss()
            }
            .create()

        alert.setOnShowListener {
            alert.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
        }

        alert.show()
    }

    // Excluir
    private fun excluir(lista: ListaModel) {
        AlertDialog
            .Builder(fragment.context)
            .setMessage("Deseja excluir esta lista ?")
            .setPositiveButton("Sim") { _, _ ->
                val model = ViewModelProviders.of(fragment)[ListaViewModel::class.java]
                model.excluirLista(lista)
            }
            .setNegativeButton("Não") { dialogInterface, _ ->
                dialogInterface.dismiss()
            }
            .show()
    }

    // insertAll
    fun insertAll(list: List<ListaModel>) {
        this.list.clear()
        this.list.addAll(list)
        notifyDataSetChanged()
    }

}
