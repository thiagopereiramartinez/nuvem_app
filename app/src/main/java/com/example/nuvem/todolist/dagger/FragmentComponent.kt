package com.example.nuvem.todolist.dagger

import androidx.fragment.app.Fragment
import com.example.nuvem.todolist.ListaTarefasFragment
import com.example.nuvem.todolist.net.RetrofitConfig
import dagger.Component

@Component(modules = [ RetrofitConfig::class ])
interface FragmentComponent {

    fun inject(fragment: ListaTarefasFragment)

}