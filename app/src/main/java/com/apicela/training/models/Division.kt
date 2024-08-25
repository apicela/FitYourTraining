package com.apicela.training.models

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Ignore
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.apicela.training.utils.converter.DivisionListConverter
import java.io.Serializable
import java.util.UUID

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = Workout::class,
            parentColumns = ["id"],
            childColumns = ["workoutId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Division(
    @PrimaryKey var id: String,
    var workoutId: String,
    var name: String,
    var image: String,
    @TypeConverters(DivisionListConverter::class)
    var listOfExercises: List<String> // armazena apenas o ID
) : Serializable {
    @Ignore
    constructor(
        workoutId: String,
        divisionName: String,
        image: String,
        listOfExercises: List<String>
    ) :
            this(UUID.randomUUID().toString(), workoutId, divisionName, image, listOfExercises)
}