package com.example.todoapp.view

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.example.todoapp.R
import com.example.todoapp.databinding.FragmentCreateTodoBinding
import com.example.todoapp.model.Todo
import com.example.todoapp.util.NotificationHelper
import com.example.todoapp.util.TodoWorker
import com.example.todoapp.viewmodel.DetailTodoViewModel
import java.util.concurrent.TimeUnit


class CreateTodoFragment : Fragment(), RadioClickListener,TodoEditClickListener {

    private lateinit var binding:FragmentCreateTodoBinding
    private lateinit var viewModel: DetailTodoViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_create_todo, container, false)
        binding = FragmentCreateTodoBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if(ActivityCompat.checkSelfPermission(requireContext(),Manifest.permission.POST_NOTIFICATIONS)!= PackageManager.PERMISSION_GRANTED){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    NotificationHelper.REQUEST_NOTIF)//arrayof ini bisa banyak permission kita minta (itu bisa dikoma sebelah notifications)
            }
        }

        binding.todo = Todo("","",3,0)
        binding.radiolistener = this
        binding.addlistener = this

        viewModel = ViewModelProvider(this).get(DetailTodoViewModel::class.java)

    }

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults) //berisi informasi permision disetuju atau tidak
        if(requestCode == NotificationHelper.REQUEST_NOTIF){
            //cek jawaban pengguna
            if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                val notif = NotificationHelper(requireContext())
                notif.creatNotification("Todo Created","A new todo has been created!, Stay Focus!")
            }
        }
    }

    override fun onTodoEditClick(v: View) {
        //notification
//            val notif = NotificationHelper(view.context)
//            notif.creatNotification("Todo Created","A new todo has been created!, Stay Focus!")

        val workRequest = OneTimeWorkRequestBuilder<TodoWorker>()
            .setInitialDelay(20, TimeUnit.SECONDS)
            .setInputData(
                workDataOf(
                    "title" to "Todo Created",
                    "message" to "Stay Focus!")
            ).build()
        WorkManager.getInstance(requireContext()).enqueue(workRequest)


//        val radio = view?.findViewById<RadioButton>(binding.radioGroupPriority.checkedRadioButtonId)
//        val todo = Todo(binding.txtTitle.text.toString(), binding.txtNotes.text.toString(), radio?.tag.toString().toInt(), 0)
//        viewModel.addTodo(todo)
        viewModel.addTodo(binding.todo!!)
        Toast.makeText(context, "Todo Created",Toast.LENGTH_SHORT).show()
        Navigation.findNavController(v).popBackStack()
    }

    override fun onRadioClick(v: View) {
        binding.todo!!.priority = v.tag.toString().toInt()
    }
}