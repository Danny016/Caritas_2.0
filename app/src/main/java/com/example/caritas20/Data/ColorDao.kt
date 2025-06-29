package com.example.caritas20.Data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ColorDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(color: ProductoColor)

    @Update
    suspend fun update(color: ProductoColor)

    @Delete
    suspend fun delete(color: ProductoColor)

    @Query("SELECT * FROM producto_color")
    fun getAllColores(): Flow<List<ProductoColor>>

    @Query("SELECT * FROM producto_color")
    suspend fun getAllColoresOnce(): List<ProductoColor>

    @Query("SELECT * FROM producto_color WHERE numero = :numero")
    suspend fun getByNumero(numero: String): ProductoColor?

    @Query("DELETE FROM producto_color")
    suspend fun deleteAll()
}
