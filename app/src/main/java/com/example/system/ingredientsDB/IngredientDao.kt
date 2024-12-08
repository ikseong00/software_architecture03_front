package com.example.system.ingredientsDB

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface IngredientDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(ingredient:Ingredient)

    @Update
    suspend fun update(ingredient:Ingredient)

    @Delete
    suspend fun delete(ingredient:Ingredient)

    //테스트용
    // 사용 x
    @Query("DELETE FROM ingredient")
    suspend fun deleteAllIngredients()

    @Query("SELECT * FROM ingredient")
    suspend fun getAll() : List<Ingredient>

    @Query("SELECT * FROM ingredient WHERE expirationDate < :expirationDate")
    suspend fun getExpiredIngredients(expirationDate : Long) : List<Ingredient>

    // 사용 x
    @Query("SELECT * FROM ingredient WHERE expirationDate >= :warningStartDate AND expirationDate <= :warningEndDate")
    suspend fun getWarningIngredients(warningStartDate: Long, warningEndDate: Long): List<Ingredient>
}