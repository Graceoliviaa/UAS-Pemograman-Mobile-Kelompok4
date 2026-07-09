package com.example.uaskelompok4.data.entity

import androidx.room.*

@Entity(
    tableName = "project_team_members",
    foreignKeys = [
        ForeignKey(entity = Project::class, parentColumns = ["id"], childColumns = ["projectId"], onDelete = ForeignKey.CASCADE),
        ForeignKey(entity = Employee::class, parentColumns = ["id"], childColumns = ["employeeId"], onDelete = ForeignKey.CASCADE)
    ],
    indices = [Index(value = ["projectId"]), Index(value = ["employeeId"])]
)
data class ProjectTeamMember(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val projectId: Int,
    val employeeId: Int,
    val roleInProject: String
)