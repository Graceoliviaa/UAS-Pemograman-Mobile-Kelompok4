package com.example.uaskelompok4.data.dao

import androidx.room.*
import com.example.uaskelompok4.data.entity.Employee

@Dao
interface EmployeeDao {
    @Query("SELECT * FROM employees ORDER BY name ASC")
    fun getAll(): List<Employee>

    @Insert
    fun insertAll(employees: List<Employee>)
}