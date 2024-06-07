package com.example.todoapp.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.icu.text.CaseMap.Title
import android.os.Build
import android.os.Message
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.todoapp.R
import com.example.todoapp.model.TodoDatabase
import com.example.todoapp.view.MainActivity

val DB_NAME = "newtododb"

fun buildDb(context: Context):TodoDatabase{
    val db = TodoDatabase.buildDatabase(context)
    return db
}

//Migration
//Migration dari versi 1 ke 2
val MIGRATION_1_2 = object: Migration(1,2){
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("ALTER TABLE todo ADD COLUMN priority INTEGER DEFAULT 3 NOT NULL")
    }

}

//Migration dari versi 2 ke 3
val MIGRATION_2_3 = object: Migration(2,3){
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("ALTER TABLE todo ADD COLUMN is_done INTEGER default 0 not null")
    }
}

class NotificationHelper(val context: Context){
    private val CHANNEL_ID = "todo_channel" //boleh diisi bebas
    private val NOTIFICATION_ID = 1 //angka sembarang

    companion object{
        val REQUEST_NOTIF = 100 //angka random
    }

    private fun createNotificationChannel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val channel = NotificationChannel(CHANNEL_ID,CHANNEL_ID,NotificationManager.IMPORTANCE_DEFAULT)
            channel.description = "Channel to publish a notification when todo created"
            val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }

    //Code bikin Notification
    fun creatNotification(title: String, message: String){
        createNotificationChannel()
        //Intent untuk mengarahkan user jika memencet notif maka aplikasi akan kebuka main activity
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        }

        val pendingIntent = PendingIntent.getActivity(context,0,intent,PendingIntent.FLAG_IMMUTABLE)
        val icon = BitmapFactory.decodeResource(context.resources, R.drawable.todochar)

        //object notif
        val notif = NotificationCompat.Builder(context,CHANNEL_ID).setSmallIcon(R.drawable.checklist)
            .setLargeIcon(icon)
            .setContentTitle(title)
            .setContentText(message)
            .setStyle(
                NotificationCompat.BigPictureStyle()
                    .bigPicture(icon)
                    .bigLargeIcon(null)
            )
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        try {
            NotificationManagerCompat.from(context).notify(NOTIFICATION_ID,notif)
        }catch (e:SecurityException){
            Log.e("errornotif",e.message.toString())
        }

    }
}

class TodoWorker(context: Context, params:WorkerParameters):Worker(context,params){
    override fun doWork(): Result {
        val judul = inputData.getString("title").toString()
        val msg = inputData.getString("message").toString()
        NotificationHelper(applicationContext).creatNotification(judul,msg)
        return Result.success()
    }

}