package com.example.nuvem.todolist.dagger

import com.example.nuvem.todolist.net.RetrofitConfig
import com.example.nuvem.todolist.viewmodels.ListaViewModel
import com.example.nuvem.todolist.viewmodels.TarefaViewModel
import dagger.Component

@Component(modules = [ RetrofitConfig::class ])
interface ViewModelComponent {

    fun inject(viewModel: ListaViewModel)

    fun inject(viewModel: TarefaViewModel)

}
