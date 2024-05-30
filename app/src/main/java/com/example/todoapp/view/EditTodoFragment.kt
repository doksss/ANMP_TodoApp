package com.example.todoapp.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.todoapp.R
import com.example.todoapp.databinding.FragmentCreateTodoBinding
import com.example.todoapp.databinding.FragmentEditTodoBinding
import com.example.todoapp.model.Todo
import com.example.todoapp.viewmodel.DetailTodoViewModel


class EditTodoFragment : Fragment(), RadioClickListener, TodoEditClickListener {

//    private lateinit var binding:FragmentCreateTodoBinding //pake layout create aja biar ga 2x
    private lateinit var binding:FragmentEditTodoBinding
    private lateinit var viewModel:DetailTodoViewModel
    private lateinit var todo:Todo //ini udah ga kepake karena udah pake data binding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentEditTodoBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(DetailTodoViewModel::class.java)
        binding.txtHeading.text = "Edit Todo"

        val uuid = EditTodoFragmentArgs.fromBundle(requireArguments()).uuid

        //melakukan Fetch
        viewModel.fetch(uuid)

        binding.radiolistener = this
        binding.submitlistener = this


//        binding.btnAdd.setOnClickListener {
//            todo.title = binding.txtTitle.text.toString()
//            todo.notes = binding.txtNotes.text.toString()
//            val radio = view.findViewById<RadioButton>(binding.radioGroupPriority.checkedRadioButtonId)
//            todo.priority = radio.tag.toString().toInt()
//            viewModel.update(todo)
//            Toast.makeText(context, "Todo Updated", Toast.LENGTH_SHORT).show()
//        }

        observeViewModel()
    }

    fun observeViewModel(){
        viewModel.todoLD.observe(viewLifecycleOwner, Observer {
//            todo = it
//            binding.txtTitle.setText(it.title)
//            binding.txtNotes.setText(it.notes)
//            //bisa pake if biasa atau when
//            when(it.priority){
//                1 -> binding.radioLow.isChecked = true
//                2 -> binding.radioMedium.isChecked = true
//                else -> binding.radioHigh.isChecked = true
//            }

            //DATA BINDING
            binding.todo = it
        })
    }

    override fun onRadioClick(v: View) {
        binding.todo!!.priority = v.tag.toString().toInt()
    }

    override fun onTodoEditClick(v: View) {
        viewModel.update(binding.todo!!)
        Toast.makeText(context,"Todo Updated",Toast.LENGTH_SHORT).show()
    }

}