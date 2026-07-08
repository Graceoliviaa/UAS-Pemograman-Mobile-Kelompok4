package com.example.uaskelompok4.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.uaskelompok4.data.entity.Project

@Dao
interface ProjectDao {
    @Query("SELECT * FROM projects ORDER BY id DESC")
    fun getAll(): LiveData<List<Project>>

    @Query("SELECT * FROM projects WHERE id = :id")
    fun getById(id: Int): Project?

    @Insert
    fun insert(project: Project): Long

    @Update
    fun update(project: Project)

    @Delete
    fun delete(project: Project)
}