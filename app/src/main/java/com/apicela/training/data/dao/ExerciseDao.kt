package com.apicela.training.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.apicela.training.models.Exercise
import com.apicela.training.models.Workout

@Dao
interface ExerciseDao {
    @Query("SELECT * FROM exercise")
    fun getAllExercises(): List<Exercise>

    @Query("SELECT * FROM exercise WHERE id = :id")
    fun getExerciseById(id: String): Exercise

    @Query("DELETE FROM Exercise WHERE id = :id")
    fun deleteById(id: String)

    @Insert
    fun insert(exercise: Exercise)

    @Update
    fun update(workout: Exercise)
}

