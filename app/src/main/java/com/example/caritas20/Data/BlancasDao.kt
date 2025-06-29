package com.example.caritas20.Data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface BlancasDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(blancas: ProductoBlanca)

    @Update
    suspend fun update(blancas: ProductoBlanca)

    @Delete
    suspend fun delete(blancas: ProductoBlanca)

    @Query("SELECT * FROM producto_blanca")
    fun getAllBlancas(): Flow<List<ProductoBlanca>>

    @Query("SELECT * FROM producto_blanca")
    suspend fun getAllBlancasOnce(): List<ProductoBlanca>

    @Query("SELECT * FROM producto_blanca WHERE numero = :numero")
    suspend fun getByNumero(numero: String): ProductoBlanca?

    @Query("DELETE FROM producto_blanca")
    suspend fun deleteAll()
}
