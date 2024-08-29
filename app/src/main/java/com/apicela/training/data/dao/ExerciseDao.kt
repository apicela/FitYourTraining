package com.apicela.training.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.apicela.training.models.Exercise

@Dao
interface ExerciseDao {
    @Query("SELECT * FROM exercise")
     fun getAll(): List<Exercise>

    @Query("SELECT * FROM exercise WHERE id = :id")
     fun getById(id: String): Exercise

    @Query("DELETE FROM Exercise WHERE id = :id")
     fun deleteById(id: String)

    @Insert
     fun insert(exercise: Exercise)

    @Update
     fun update(workout: Exercise)
}

