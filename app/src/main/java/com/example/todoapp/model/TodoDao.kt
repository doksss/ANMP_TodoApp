package com.example.todoapp.model

import android.icu.text.CaseMap.Title
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface TodoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg todo: Todo)

    @Query("SELECT * FROM todo ORDER BY priority desc")
    fun selectAllTodo():List<Todo>

    @Query("SELECT * FROM todo WHERE uuid= :id")
    fun selectTodo(id:Int):Todo

    @Delete
    fun deleteTodo(todo:Todo)

    @Update //boleh pake Query manual mau delete,insert,ataupun update
    fun updateTodo(todo:Todo)

    //ini update yg pake Query Manual boleh aja pake yg mana
    @Query("Update todo set title=:title, notes=:notes, priority=:priority where uuid=:uuid")
    fun update(title: String, notes:String, priority:Int,uuid:Int)
}