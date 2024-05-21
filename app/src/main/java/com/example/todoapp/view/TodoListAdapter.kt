package com.example.todoapp.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.databinding.TodoItemLayoutBinding
import com.example.todoapp.model.Todo

class TodoListAdapter(val todoList:ArrayList<Todo>)
    : RecyclerView.Adapter<TodoListAdapter.TodoViewHolder>() {
    class TodoViewHolder(var binding: TodoItemLayoutBinding):
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val binding = TodoItemLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return TodoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int)
    {
        holder.binding.checkTask.text = todoList[position].title
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
}