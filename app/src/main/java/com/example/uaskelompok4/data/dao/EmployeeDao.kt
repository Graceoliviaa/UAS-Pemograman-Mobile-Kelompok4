package com.example.uaskelompok4.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.uaskelompok4.data.Employee

@Dao
interface EmployeeDao {

    @Query("SELECT * FROM employees")
    fun getAll(): List<Employee>

    @Insert
    fun insert(employee: Employee)

    @Delete
    fun delete(employee: Employee)
}