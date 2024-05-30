package com.example.todoapp.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.databinding.TodoItemLayoutBinding
import com.example.todoapp.model.Todo

class TodoListAdapter(val todoList:ArrayList<Todo>, val adapterOnClick: (Todo)->Unit)
    : RecyclerView.Adapter<TodoListAdapter.TodoViewHolder>()
    , TodoCheckedChangeListener, TodoEditClickListener {
    class TodoViewHolder(var binding: TodoItemLayoutBinding):
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val binding = TodoItemLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return TodoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int)
    {

        //DATA BINDING Week 10
        //setiap buat variable baru harus di inisialisasi disini
        holder.binding.todo = todoList[position]

        holder.binding.listener = this
        holder.binding.checkTask.isChecked = false //agar yg bawah tidak check

        holder.binding.editlistener = this

        //VIEW BINDING
//        holder.binding.checkTask.text = todoList[position].title
//        holder.binding.checkTask.isChecked = false //agar yg bawah tidak ikut kencentang ini bug
//        holder.binding.checkTask.setOnCheckedChangeListener { compoundButton, b ->
//            if(compoundButton.isPressed){
//                adapterOnClick(todoList[position])
//            }
//        }
//        holder.binding.imgEdit.setOnClickListener{
//            val action = TodoListFragmentDirections.actionEditTodo(todoList[position].uuid)
//            Navigation.findNavController(it).navigate(action)
//        }
    }

    override fun getItemCount(): Int {
        return todoList.size
    }

    //untuk replace todoList yg lama dengan yg baru
    fun updateTodoList(newTodoList:List<Todo>){
        todoList.clear()
        todoList.addAll(newTodoList)
        notifyDataSetChanged()
    }

    override fun onTodoCheckedChange(cb: CompoundButton, isChecked: Boolean, todo: Todo) {
        if(cb.isPressed){
            adapterOnClick(todo)
        }
    }

    override fun onTodoEditClick(v: View) {
        val uuid = v.tag.toString().toInt()
        //navigasi
        val action = TodoListFragmentDirections.actionEditTodo(uuid)
        Navigation.findNavController(v).navigate(action)
    }
}