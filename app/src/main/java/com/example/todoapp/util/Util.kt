package com.example.todoapp.util

import android.content.Context
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.todoapp.model.TodoDatabase

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