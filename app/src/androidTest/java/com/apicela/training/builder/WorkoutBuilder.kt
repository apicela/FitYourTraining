package com.apicela.training.builder

import com.apicela.training.models.Workout
import com.github.javafaker.Faker

class WorkoutBuilder {
    val faker = Faker()
    private var name: String = faker.dragonBall().toString()
    private var description: String = faker.hacker().toString()
    private var image: String = faker.internet().image()
    private var listOfDivision: List<String> = emptyList()

    fun name(name: String) = apply { this.name = name }
    fun description(description: String) = apply { this.description = description }
    fun image(image: String) = apply { this.image = image }
    fun listOfDivision(listOfDivision: List<String>) =
        apply { this.listOfDivision = listOfDivision }

    fun build(): Workout {
        return Workout(name, description, image, listOfDivision)
    }
}