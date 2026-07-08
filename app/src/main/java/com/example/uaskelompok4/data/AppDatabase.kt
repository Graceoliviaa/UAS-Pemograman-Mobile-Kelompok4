package com.example.uaskelompok4.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.uaskelompok4.data.dao.ClientDao
import com.example.uaskelompok4.data.dao.ProjectDao
import com.example.uaskelompok4.data.dao.ProjectDocumentDao
import com.example.uaskelompok4.data.entity.Client
import com.example.uaskelompok4.data.entity.Project
import com.example.uaskelompok4.data.entity.ProjectDocument

@Database(
    entities = [
        Project::class,
        Client::class,
        ProjectDocument::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun projectDao(): ProjectDao

    abstract fun clientDao(): ClientDao

    abstract fun projectDocumentDao(): ProjectDocumentDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "uaskelompok4_db"
                ).build().also { INSTANCE = it }
            }
        }
    }
}