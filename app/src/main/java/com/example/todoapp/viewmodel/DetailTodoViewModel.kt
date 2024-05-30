package com.example.todoapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.todoapp.model.Todo
import com.example.todoapp.model.TodoDatabase
import com.example.todoapp.util.buildDb
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class DetailTodoViewModel(application: Application):AndroidViewModel(application),CoroutineScope {

    private val job=Job()
    val todoLD = MutableLiveData<Todo>()
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.IO

    fun fetch(uuid:Int){
        launch {
            val db = buildDb(getApplication())
            todoLD.postValue(db.todoDao().selectTodo(uuid))
        }
    }

    //bisa query biasa ga dijadikan object
    fun update(todo: Todo){
        launch {
            buildDb(getApplication()).todoDao().updateTodo(todo)
        }
    }
    fun addTodo(todo: Todo){
        launch {
//            val db = TodoDatabase.buildDatabase(getApplication())
            val db = buildDb(getApplication())
            db.todoDao().insertAll(todo)
        }
    }
}