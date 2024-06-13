package com.example.todoapp.view

import android.view.View
import android.widget.CompoundButton
import com.example.todoapp.model.Todo

//nama Interface Bebas
interface TodoCheckedChangeListener {
    //Nama bebas
    fun onTodoCheckedChange(cb:CompoundButton, isChecked:Boolean, todo:Todo)
}

interface TodoEditClickListener{
    fun onTodoEditClick(v:View)
}

interface RadioClickListener{
    fun onRadioClick(v: View)
}

interface DateClickListener{
    fun onDateClick(v: View)
}

interface TimeClickListener{
    fun onTimeClick(v: View)
}