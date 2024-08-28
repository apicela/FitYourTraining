package com.apicela.training.builder

import com.apicela.training.models.Exercise
import com.apicela.training.models.extra.Metrics
import com.apicela.training.models.extra.Muscle
import com.github.javafaker.Faker

class ExerciseBuilder {
    val faker = Faker()
    private var name: String = faker.dragonBall().toString()
    private var image: String = faker.internet().image()
    private var metrics: Metrics = Metrics.CARGA
    private var muscle: Muscle = Muscle.CHEST

    fun withName(name: String) = apply { this.name = name }
    fun withImage(image: String) = apply { this.image = image }
    fun withMuscle(muscle: Muscle) = apply { this.muscle = muscle }
    fun withMetrics(metrics: Metrics) = apply { this.metrics = metrics }

    fun build() = Exercise(name, image, muscle , metrics)
}