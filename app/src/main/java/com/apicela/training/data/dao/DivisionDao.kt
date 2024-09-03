package com.apicela.training.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.apicela.training.models.Division
import com.apicela.training.models.Exercise

@Dao
interface DivisionDao {
    @Query("SELECT * FROM division")
    suspend fun getAll(): List<Division>

    @Insert
    suspend fun insert(division: Division)

    @Query("SELECT * FROM division WHERE id = :divisionId")
    suspend fun getById(divisionId: String): Division?

    @Update
    suspend fun update(division: Division)

    @Query("DELETE FROM Division WHERE id = :id")
    suspend fun deleteById(id: String)

    @Query("UPDATE division SET listOfExercises = :listOfExercises WHERE id = :divisionId")
    suspend fun updateListOfExercises(divisionId: String, listOfExercises: List<Exercise>)
}

