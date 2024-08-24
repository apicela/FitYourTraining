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
    fun getAllDivisions(): List<Division>

    @Insert
    fun insert(division: Division)

    @Query("SELECT * FROM division WHERE id = :divisionId")
    fun getDivisionById(divisionId: String): Division?

    @Update
    fun update(division: Division)

    @Query("DELETE FROM Division WHERE id = :id")
    fun deleteById(id: String)

    @Query("UPDATE division SET listOfExercises = :listOfExercises WHERE id = :divisionId")
    fun updateListOfExercises(divisionId: String, listOfExercises: List<Exercise>)
}

