package com.example.uaskelompok4.data.dao

import androidx.room.*
import com.example.uaskelompok4.data.entity.ProjectTeamMember

@Dao
interface ProjectTeamMemberDao {
    @Query("SELECT * FROM project_team_members WHERE projectId = :projectId")
    fun getByProject(projectId: Int): List<ProjectTeamMember>

    @Insert
    fun insert(member: ProjectTeamMember): Long

    @Delete
    fun delete(member: ProjectTeamMember)

    @Query("DELETE FROM project_team_members WHERE projectId = :projectId AND employeeId = :employeeId")
    fun deleteByProjectAndEmployee(projectId: Int, employeeId: Int)
}