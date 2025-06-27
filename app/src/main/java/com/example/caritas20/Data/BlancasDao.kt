package com.example.caritas20.Data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface BlancasDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(blancas: Blancas)

    @Update
    suspend fun update(blancas: Blancas)

    @Delete
    suspend fun delete(blancas: Blancas)

    @Query("SELECT * FROM Blancas")
    fun getAllBlancas(): Flow<List<Blancas>>

    @Query("SELECT * FROM Blancas WHERE numero = :numero")
    suspend fun getByNumero(numero: Int): Blancas?

    @Query("DELETE FROM Blancas")
    suspend fun deleteAll()
}
