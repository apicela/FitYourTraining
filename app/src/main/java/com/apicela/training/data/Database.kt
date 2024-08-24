package com.apicela.training.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.apicela.training.data.dao.DivisionDao
import com.apicela.training.data.dao.ExecutionDao
import com.apicela.training.data.dao.ExerciseDao
import com.apicela.training.data.dao.ObservationDao
import com.apicela.training.data.dao.WorkoutDao
import com.apicela.training.models.Division
import com.apicela.training.models.Execution
import com.apicela.training.models.Exercise
import com.apicela.training.models.Observation
import com.apicela.training.models.Workout
import com.apicela.training.utils.converter.Converter
import com.apicela.training.utils.converter.DivisionListConverter
import com.apicela.training.utils.converter.ExerciseListConverter

@Database(
    entities = [Exercise::class, Workout::class, Division::class, Execution::class, Observation::class],
    version = 6,
    exportSchema = false
)
@TypeConverters(
    ExerciseListConverter::class, DivisionListConverter::class, Converter::class
)


abstract class Database : RoomDatabase() {
    abstract fun divisionDao(): DivisionDao
    abstract fun exerciseDao(): ExerciseDao
    abstract fun workoutDao(): WorkoutDao
    abstract fun executionDao(): ExecutionDao
    abstract fun observationDao(): ObservationDao

}