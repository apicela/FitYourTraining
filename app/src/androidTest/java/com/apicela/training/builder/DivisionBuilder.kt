package com.apicela.training.builder

import com.apicela.training.models.Division
import com.github.javafaker.Faker

class DivisionBuilder {
    val faker = Faker()
    private var workoutId: String = ""
    private var name: String = faker.animal().name()
    private var image: String = faker.internet().image()
    private var listOfExercises: List<String> = emptyList()

    fun workoutId(workoutId: String) = apply { this.workoutId = workoutId }
    fun name(name: String) = apply { this.name = name }
    fun image(image: String) = apply { this.image = image }
    fun listOfExercises(listOfExercises: List<String>) = apply { this.listOfExercises = listOfExercises }

    fun build(): Division {
        return Division( workoutId, name, image, listOfExercises)
    }
}