package com.example.todoapp.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Todo(
    @ColumnInfo(name = "title")
    var title: String,
    @ColumnInfo(name = "notes")
    var notes:String,
    @ColumnInfo(name="priority")
    var priority:Int,
    @ColumnInfo(name="is_done")
    var is_done:Int, //menggunakan int karena sqlite tidak ada tipe boolean adanya int
    @ColumnInfo(name = "todo_date")
    var todo_date:Int, //menggunakan int karena sqlite tidak ada date, maka pake UNIXTIMESTAMP
){
    @PrimaryKey(autoGenerate = true)
    var uuid:Int = 0
}