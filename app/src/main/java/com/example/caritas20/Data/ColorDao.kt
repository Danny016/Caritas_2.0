package com.example.caritas20.Data

import androidx.room.*

@Dao
interface ColorDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(color: Color)

    @Update
    suspend fun update(color: Color)

    @Delete
    suspend fun delete(color: Color)

    @Query("SELECT * FROM Color")
    suspend fun getAll(): List<Color>

    @Query("SELECT * FROM Color WHERE id = :id")
    suspend fun getById(id: String): Color?
}
