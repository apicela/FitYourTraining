package com.apicela.training.models.extra

import android.util.Log

data class ExecutionInfo(
    val month: String,
    val list: List<Info>
) {
    fun convertToAverage(): ExecutionRaw? {
        // Função para calcular a média
        fun <T : Number> calculateAverage(values: List<T>): Double {
            val avg = (values.sumOf { it.toDouble() } / values.size)
             return String.format("%.2f", avg).toDouble()
        }

        // Extrai os valores de kg e repetitions da lista e calcula a média
        val avgKg = calculateAverage(list.map { it.kg }).toFloat()
        val avgRepetitions = calculateAverage(list.map { it.repetitions }).toFloat()
        val info = Info(avgKg, avgRepetitions)
        return ExecutionRaw(month, info.kg, info.repetitions)
    }

    fun convertToMaxWeight(): ExecutionRaw? {
        val info = list.maxWith(compareBy<Info> { it.kg }.thenBy { it.repetitions })
        return ExecutionRaw(month, info.kg, info.repetitions)
    }

    fun convertToModeOrMaxWeight(): ExecutionRaw? {
        // Cria um mapa para contar as ocorrências de cada Info
        val infoCountMap = mutableMapOf<Float, Int>()

        // Preenche o mapa com as contagens
        list.forEach { info ->
            infoCountMap[info.kg] = infoCountMap.getOrDefault(info.kg, 0) + 1
        }
        Log.d("ExecutionSubClass", "infoCountMap: $infoCountMap")

        // Encontra o maior valor de contagem
        val kgMode = infoCountMap.maxWithOrNull(compareBy<Map.Entry<Float, Int>> { it.value }
            .thenBy { it.key })?.key
        val repsCountMap = mutableMapOf<Float, Int>()
        list.forEach {
            if(it.kg == kgMode){
                repsCountMap[it.repetitions] = repsCountMap.getOrDefault(it.repetitions, 0) + 1
            }
        }
        val repsMode =  repsCountMap.maxBy { it.value }.key
        val info = Info(kgMode!!, repsMode)
        Log.d("ExecutionSubClass", "info: $info")
        return info?.let { ExecutionRaw(month, it.kg, it.repetitions) }
    }
}



data class Info(
    val kg: Float,
    val repetitions: Float
)

data class ExecutionRaw(
    val month: String,
    val kg: Float,
    val repetitions: Float
)

