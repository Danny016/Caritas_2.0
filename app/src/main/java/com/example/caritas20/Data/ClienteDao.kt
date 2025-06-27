package com.example.caritas20.Data

import androidx.room.*

@Dao
interface ClienteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(cliente: Cliente)

    @Update
    suspend fun update(cliente: Cliente)

    @Delete
    suspend fun delete(cliente: Cliente)

    @Query("SELECT * FROM Cliente")
    suspend fun getAll(): List<Cliente>

    @Query("SELECT * FROM Cliente WHERE id_cliente = :id")
    suspend fun getById(id: Int): Cliente?
}
