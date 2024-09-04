package com.apicela.training.models

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import java.util.UUID

@Entity
data class Workout(
    @PrimaryKey var id: String,
    var name: String,
    var description: String,
    var image: String,
    var listOfDivision: List<String>
) {
    @Ignore
    constructor(
        name: String,
        description: String,
        image: String,
        listOfDivision: List<String>? = emptyList()
    ) :
            this(
                UUID.randomUUID().toString(),
                name,
                description,
                image,
                listOfDivision ?: emptyList()
            )

}