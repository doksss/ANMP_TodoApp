package com.example.todoapp.view

import android.Manifest
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.RadioButton
import android.widget.TimePicker
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
import java.util.Calendar
import java.util.concurrent.TimeUnit


class CreateTodoFragment : Fragment(), RadioClickListener,TodoEditClickListener,DateClickListener,TimeClickListener,
    DatePickerDialog.OnDateSetListener,
TimePickerDialog.OnTimeSetListener{

    private lateinit var binding:FragmentCreateTodoBinding
    private lateinit var viewModel: DetailTodoViewModel

    var year = 0
    var month = 0
    var day = 0
    var hour =0
    var minute = 0

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

        //ngecek permission kek ALLOW OR DENY
        if(ActivityCompat.checkSelfPermission(requireContext(),Manifest.permission.POST_NOTIFICATIONS)!= PackageManager.PERMISSION_GRANTED){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    NotificationHelper.REQUEST_NOTIF)//arrayof ini bisa banyak permission kita minta (itu bisa dikoma sebelah notifications)
            }
        }

        binding.todo = Todo("","",3,0,0)
        binding.radiolistener = this
        binding.addlistener = this
        binding.datelistener = this
        binding.timelistener = this

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

        val today = Calendar.getInstance() //calendar hari ini
        val c = Calendar.getInstance() //Calendar yg di set pengguna
        c.set(year,month,day,hour,minute,0)
        val delay = (c.timeInMillis/1000L)-(today.timeInMillis/1000L)
        //mengupdate field value todo_date di db
        binding.todo!!.todo_date = (c.timeInMillis/1000L).toInt()

        //notification
//            val notif = NotificationHelper(view.context)
//            notif.creatNotification("Todo Created","A new todo has been created!, Stay Focus!")

        val workRequest = OneTimeWorkRequestBuilder<TodoWorker>()
            .setInitialDelay(delay, TimeUnit.SECONDS)
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

    //2 ini (onDateClick dan onTimeClick) hanya untuk trigger menampilkan dialog
    override fun onDateClick(v: View) {
        val c = Calendar.getInstance() //mendapatkan hari ini
        val y = c.get(Calendar.YEAR)
        val m = c.get(Calendar.MONTH)
        val d = c.get(Calendar.DAY_OF_MONTH)

        //tanggal bulan tahun hari ini
        DatePickerDialog(requireContext(),this,y,m,d).show()

    }

    override fun onTimeClick(v: View) {
        val c = Calendar.getInstance() //mendapatkan hari ini
        val hour = c.get(Calendar.HOUR)
        val min = c.get(Calendar.MINUTE)
        //true maka menggunakan format 24 jam, kalo false menggunakan format 12 jam (20.00 jadi 08.00 pm)
        TimePickerDialog(requireContext(),this,hour,min,true).show()
    }
    //2 ini (onDateSet dan onTimeSet) digunakan untuk membaca tanggal dan jam pilihan user
    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        //object calendar yg defaultnya hari ini lalu diganti tanggal sesuai dgn isi parameter
        Calendar.getInstance().let {
            it.set(year,month,dayOfMonth)
            //Padstart gunanya untuk menambahkan leading zero jadi misal skrg bulan 6 maka akan menjadi 06,
            //karena karakter msh 1 maka menambahkan 0 didepan
            binding.txtDate.setText(dayOfMonth.toString().padStart(2,'0')+"-"+
                    (month+1).toString().padStart(2,'0')+"-"+year)
            //disini januari kebacanya 0 dan desember 11 maka di +1
            //jika mau ganti ke unixtimestamp gausa di +1 nanti
            this.year = year
            this.month = month
            this.day= dayOfMonth
        }
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        binding.txtTime.setText(hourOfDay.toString().padStart(2,'0')+":"+minute.toString().padStart(2,'0'))
        this.hour = hourOfDay
        this.minute=minute
    }
}