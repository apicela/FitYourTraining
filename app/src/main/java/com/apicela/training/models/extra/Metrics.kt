package com.apicela.training.models.extra

enum class Metrics(val ptbr : String) {
    CARDIO("Tempo"),
    CARGA("Carga e repetição");


    companion object {

        fun getAsListPTBR(): List<String> {
            return Metrics.values().map { it.ptbr }
        }

        fun getMetricByPtbr(metric: String): String {
            return Metrics.values().find { it.toString() == metric }!!.ptbr
        }

        fun getMetricPTBRtoENG(metric: String): Metrics? {
            return Metrics.values().find { it.ptbr.toString() == metric }
        }
    }
}

