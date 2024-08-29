package com.apicela.training.models.extra

data class ExecutionInfo(
    val month: String,
    val list: List<Info>
)

data class Info(
    val kg: Float,
    val repetitions: Int
)

data class ExecutionRaw(
    val month: String,
    val kg: Float,
    val repetitions: Int
)
