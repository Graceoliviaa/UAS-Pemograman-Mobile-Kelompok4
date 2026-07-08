package com.example.uaskelompok4.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.uaskelompok4.data.entity.Client

@Dao
interface ClientDao {
    @Query("SELECT * FROM clients ORDER BY id DESC")
    fun getAll(): LiveData<List<Client>>

    @Query("SELECT * FROM clients WHERE id = :id")
    fun getById(id: Int): Client?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(client: Client): Long

    @Update
    fun update(client: Client)

    @Delete
    fun delete(client: Client)
}
