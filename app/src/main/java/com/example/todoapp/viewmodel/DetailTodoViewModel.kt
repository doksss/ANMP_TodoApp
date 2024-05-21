package com.example.todoapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.todoapp.model.Todo
import com.example.todoapp.model.TodoDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class DetailTodoViewModel(application: Application):AndroidViewModel(application),CoroutineScope {

    private val job=Job()
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.IO

    fun addTodo(todo: Todo){
        launch {
            val db = TodoDatabase.buildDatabase(getApplication())
            db.todoDao().insertAll(todo)
        }
    }
}