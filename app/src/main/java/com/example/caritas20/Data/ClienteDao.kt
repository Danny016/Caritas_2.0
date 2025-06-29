package com.example.caritas20.Data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ClienteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(cliente: Cliente)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAndGetId(cliente: Cliente): Long

    @Update
    suspend fun update(cliente: Cliente)

    @Delete
    suspend fun delete(cliente: Cliente)

    @Query("SELECT * FROM Cliente")
    fun getAllClientes(): Flow<List<Cliente>>

    @Query("SELECT * FROM Cliente WHERE id_cliente = :id")
    suspend fun getById(id: Int): Cliente?

    @Query("DELETE FROM Cliente")
    suspend fun deleteAll()
}
