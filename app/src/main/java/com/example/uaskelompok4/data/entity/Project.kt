package com.example.uaskelompok4.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "projects")
data class Project(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String = "",
    val description: String = "",
    val status: String = "Perencanaan",
    val startDate: String = "",
    val endDate: String? = null,
    val clientId: Int? = null
)
