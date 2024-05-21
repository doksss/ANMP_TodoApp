package com.example.todoapp.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todoapp.R
import com.example.todoapp.databinding.FragmentTodoListBinding
import com.example.todoapp.databinding.TodoItemLayoutBinding
import com.example.todoapp.viewmodel.ListTodoViewModel


class TodoListFragment : Fragment() {

    private lateinit var binding: FragmentTodoListBinding
    private var adapter = TodoListAdapter(arrayListOf(), {todo -> viewModel.clearTask(todo) })
    private lateinit var viewModel:ListTodoViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTodoListBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(ListTodoViewModel::class.java)
        viewModel.select()
        binding.recViewTodo.layoutManager = LinearLayoutManager(context)
        binding.recViewTodo.adapter = adapter

        binding.btnFab.setOnClickListener {
            val action = TodoListFragmentDirections.actionCreateTodo()
            Navigation.findNavController(it).navigate(action)
        }

        observeViewModel()
    }

    fun observeViewModel(){
        viewModel.todoListLD.observe(viewLifecycleOwner, Observer {
            adapter.updateTodoList(it)

            if(it.isEmpty()){
                binding.txtError.text = "Todo still empty"
                binding.txtError.visibility = View.VISIBLE
            }
        })

        viewModel.loadingLD.observe(viewLifecycleOwner, Observer {
           if(it==false){
               binding.progressBar.visibility = View.GONE
           }else{
               binding.progressBar.visibility = View.VISIBLE
           }
        })

        viewModel.todoLoadErrorLD.observe(viewLifecycleOwner, Observer {
            if(it==false){
                binding.txtError.visibility = View.GONE
            }else{
                binding.txtError.visibility = View.VISIBLE
                binding.txtError.text = "An error occurred"
            }
        })
    }
}