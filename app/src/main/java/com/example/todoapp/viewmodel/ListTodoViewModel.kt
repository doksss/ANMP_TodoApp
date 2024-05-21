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

class ListTodoViewModel(application: Application):AndroidViewModel(application),CoroutineScope {
    val todoListLD = MutableLiveData<List<Todo>>()
    val todoLoadErrorLD = MutableLiveData<Boolean>()
    val loadingLD = MutableLiveData<Boolean>()

    private var job = Job()

    override val coroutineContext:CoroutineContext
        get() = job +Dispatchers.IO

    //buat function untuk melakukan sesuatu
    //function select semua data Todo
    fun select(){
        loadingLD.value = true
        todoLoadErrorLD.value = false
        launch {
//            val db = TodoDatabase.buildDatabase(getApplication())
            val db = buildDb(getApplication())
            todoListLD.postValue(db.todoDao().selectAllTodo())
            loadingLD.postValue(false)
        }

    }
    //function dipanggil jika mencentang Todonya
    fun clearTask(todo: Todo){
        launch {
//            val db = TodoDatabase.buildDatabase(getApplication())
            val db = buildDb(getApplication())
            db.todoDao().deleteTodo(todo)
            todoListLD.postValue(db.todoDao().selectAllTodo())
        }
    }

}