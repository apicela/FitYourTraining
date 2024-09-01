package com.apicela.training.models.extra

data class ExecutionInfo(
    val month: String,
    val list: List<Info>
) {
    fun convertToAverage(): ExecutionRaw? {
        // Função para calcular a média
        fun <T : Number> calculateAverage(values: List<T>): Double {
            return values.sumOf { it.toDouble() } / values.size
        }

        // Extrai os valores de kg e repetitions da lista e calcula a média
        val avgKg = calculateAverage(list.map { it.kg })
        val avgRepetitions = calculateAverage(list.map { it.repetitions }).toFloat()
        val info = Info(avgKg.toFloat(), avgRepetitions)
        return ExecutionRaw(month, info.kg, info.repetitions)
    }

    fun convertToMaxWeight(): ExecutionRaw? {
        val info = list.maxBy { info -> info.kg  }
        return ExecutionRaw(month, info.kg, info.repetitions)
    }

    fun convertToModeOrMaxWeight(): ExecutionRaw? {
        // Cria um mapa para contar as ocorrências de cada Info
        val infoCountMap = mutableMapOf<Info, Int>()

        // Preenche o mapa com as contagens
        list.forEach { info ->
            infoCountMap[info] = infoCountMap.getOrDefault(info, 0) + 1
        }

        // Encontra o maior valor de contagem
        val maxCount = infoCountMap.values.maxOrNull() ?: return null

        // Filtra os Info que possuem a maior contagem
        val mostFrequentInfos = infoCountMap.filter { it.value == maxCount }.keys

        // Se houver apenas um Info com a maior contagem, retorna-o
        val info = if (mostFrequentInfos.size == 1) {
            mostFrequentInfos.first()
        } else {
            mostFrequentInfos.maxByOrNull { it.kg }  // Caso contrário, retorna o Info com o maior valor de kg
        }

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

