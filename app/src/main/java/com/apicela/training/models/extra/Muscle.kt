package com.apicela.training.models.extra

enum class Muscle(val ptbr: String) {
    CHEST("Peitoral"), SHOULDER("Ombro"), BACK("Costas"), BICEPS("Bíceps"), TRICEPS("Tríceps"),
    OTHER("Outros"), ABDOMINAL("Abdominal"),
    QUADRICEPS("Quadríceps"), HAMSTRING("Posterior de Coxa"), CALVES("Panturrilha"), GLUTES("Glúteos"),
    CARDIO("Cardio");

    companion object {
        fun getAsList(): List<Muscle> {
            return values().toList()
        }

        fun getAsListPTBR(): List<String> {
            return values().map { it.ptbr }
        }

        fun getMuscleByPtbr(muscle: String): String {
            return Muscle.values().find { it.toString() == muscle }!!.ptbr
        }

        fun getMusclePTBRtoENG(muscle: String): Muscle? {
            return Muscle.values().find { it.ptbr.toString() == muscle }
        }
    }
}