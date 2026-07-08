package com.example.uaskelompok4.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.uaskelompok4.data.dao.*
import com.example.uaskelompok4.data.entity.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        Project::class,
        Client::class,
        Employee::class,
        ProjectTeamMember::class,
        ProjectDocument::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun projectDao(): ProjectDao
    abstract fun clientDao(): ClientDao
    abstract fun employeeDao(): EmployeeDao
    abstract fun projectTeamMemberDao(): ProjectTeamMemberDao
    abstract fun projectDocumentDao(): ProjectDocumentDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "project_service_db"
                )
                    .addCallback(SeedCallback(context))
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }

    private class SeedCallback(private val context: Context) : Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            CoroutineScope(Dispatchers.IO).launch {
                val database = getDatabase(context)
                // Data dummy karyawan (merepresentasikan data dari Employee Service)
                database.employeeDao().insertAll(
                    listOf(
                        Employee(name = "Andi Rahman", position = "Backend Developer", department = "IT"),
                        Employee(name = "Siti Nuraini", position = "UI/UX Designer", department = "IT"),
                        Employee(name = "Dedi Prasetyo", position = "QA Engineer", department = "IT"),
                        Employee(name = "Rina Wijaya", position = "Project Manager", department = "IT")
                    )
                )
            }
        }
    }
}