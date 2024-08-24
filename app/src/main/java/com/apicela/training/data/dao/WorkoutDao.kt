package com.apicela.training.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.apicela.training.models.Workout

@Dao
interface WorkoutDao {
    @Query("SELECT * FROM workout")
    fun getAllWorkouts(): List<Workout>

    @Query("SELECT * FROM workout WHERE id = :workoutId")
    fun getWorkoutById(workoutId: String): Workout

    @Insert
    fun insert(workout: Workout)

    @Query("DELETE FROM workout WHERE id = :id")
    fun deleteById(id: String)

    @Update
    fun update(workout: Workout)
}