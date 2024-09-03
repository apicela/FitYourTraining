package com.apicela.training.models.extra

import android.util.Log
import java.text.NumberFormat
import java.text.ParseException
import java.util.Locale

data class ExecutionInfo(
    val month: String,
    val list: List<Info>
) {
    fun convertToAverage(): ExecutionRaw? {
        fun formatNumber(value: Double, locale: Locale = Locale.US): Float {
            val numberFormat = NumberFormat.getNumberInstance(locale)
            numberFormat.maximumFractionDigits = 2
            numberFormat.minimumFractionDigits = 2
            return numberFormat.format(value).toFloat()
        }


        // Função para calcular a média
        fun <T : Number> calculateAverage(values: List<T>): Float {
            val avg = values.sumOf { it.toDouble() } / values.size
            // Formata o número com duas casas decimais usando o Locale padrão do dispositivo
            val formattedAvg = formatNumber(avg)
            Log.d("ExecutionSubClass", "formattedAvg: $formattedAvg")
            // Tenta converter o valor formatado de volta para Double usando o Locale padrão
            return formattedAvg
        }

        // Extrai os valores de kg e repetitions da lista e calcula a média
        val avgKg = calculateAverage(list.map { it.kg })
        val avgRepetitions = calculateAverage(list.map { it.repetitions })
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

