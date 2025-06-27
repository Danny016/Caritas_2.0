package com.example.caritas20.Data

import androidx.room.*

@Dao
interface BlancasDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(blancas: Blancas)

    @Update
    suspend fun update(blancas: Blancas)

    @Delete
    suspend fun delete(blancas: Blancas)

    @Query("SELECT * FROM Blancas")
    suspend fun getAll(): List<Blancas>

    @Query("SELECT * FROM Blancas WHERE id = :id")
    suspend fun getById(id: String): Blancas?
}
