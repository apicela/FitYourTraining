package com.apicela.training.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.apicela.training.models.Observation

@Dao
interface ObservationDao {
    @Query("SELECT * FROM observation WHERE strftime('%d/%m/%Y', date / 1000, 'unixepoch') =  :date")
    suspend fun getObservationByDate(date: String): Observation?

    @Query("SELECT * FROM observation WHERE date = :date")
    suspend fun getObservationByDate(date: Long): Observation?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(observation: Observation)

    @Update
    suspend fun update(observation: Observation)

}