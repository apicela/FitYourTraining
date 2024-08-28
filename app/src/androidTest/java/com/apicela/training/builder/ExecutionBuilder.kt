package com.apicela.training.builder

import com.apicela.training.models.Execution
import com.github.javafaker.Faker
import java.util.Date
import java.util.Random

class ExecutionBuilder {
    val faker = Faker()
    val random = Random()
    private var isCardio: Boolean = false
    private var repetitions: Int = random.nextInt(12)
    private var kg: Float = random.nextFloat() * random.nextInt(30)
    private var exerciseId: String = ""
    private var date: Date = Date()

    fun isCardio(isCardio: Boolean) = apply { this.isCardio = isCardio }
    fun repetitions(repetitions: Int) = apply { this.repetitions = repetitions }
    fun kg(kg: Float) = apply { this.kg = kg }
    fun exerciseId(exerciseId: String) = apply { this.exerciseId = exerciseId }
    fun date(date: Date) = apply { this.date = date }

    fun build(): Execution {
        return Execution(isCardio, repetitions, kg, exerciseId, date)
    }
}