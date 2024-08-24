package com.apicela.training.models

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import java.util.Date
import java.util.UUID

@Entity
data class Execution(
    @PrimaryKey var id: String,
    var isCardio : Boolean,
    var repetitions: Int,
    var kg: Float,
    val exercise_id: String,
    var date: Date
) {
    @Ignore
    constructor(repetitions: Int, isCardio: Boolean, kg: Float, exercise_id: String, date: Date) :
            this(UUID.randomUUID().toString(), isCardio, repetitions, kg, exercise_id, date)
}
