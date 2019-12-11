package com.example.nuvem.todolist

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.textfield.TextInputEditText
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Ação do botão Novo
        btnNovo.setOnClickListener { novaAnotacao() }
    }

    // Abrir uma caixa de diálogo com o campo para incluir uma nova anotação
    private fun novaAnotacao() {
        val sheetDialog = BottomSheetDialog(this)
        val sheetView = layoutInflater.inflate(R.layout.bottom_sheet_nova_tarefa, null)

        sheetDialog.setContentView(sheetView)
        sheetDialog.show()
    }

}
