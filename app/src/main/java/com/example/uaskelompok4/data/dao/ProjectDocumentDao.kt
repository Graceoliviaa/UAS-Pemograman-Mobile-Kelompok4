package com.example.uaskelompok4.data.dao

import androidx.room.*
import com.example.uaskelompok4.data.entity.ProjectDocument

@Dao
interface ProjectDocumentDao {
    @Query("SELECT * FROM project_documents WHERE projectId = :projectId ORDER BY id DESC")
    fun getByProject(projectId: Int): List<ProjectDocument>

    @Insert
    fun insert(document: ProjectDocument): Long

    @Delete
    fun delete(document: ProjectDocument)
}