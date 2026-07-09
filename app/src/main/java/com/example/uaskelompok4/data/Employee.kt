package com.example.uaskelompok4.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "employees")
data class Employee(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val name: String = "",

    val position: String = "",

    val department: String = ""
)