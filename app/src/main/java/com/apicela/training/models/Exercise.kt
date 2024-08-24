package com.apicela.training.models

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.apicela.training.models.extra.Metrics
import com.apicela.training.models.extra.Muscle
import java.io.Serializable
import java.util.UUID

@Entity
data class Exercise(
    @PrimaryKey var id: String,
    var name: String,
    var image: String,
    var muscleType: Muscle,
    var metricType: Metrics
) :
    Serializable {
    @Ignore
    constructor(exerciseName: String, image: String, muscleType: Muscle, metricType : Metrics) :
            this(UUID.randomUUID().toString(), exerciseName, image, muscleType, metricType)

    companion object {
        val listaExercises: MutableList<Exercise> by lazy {
            mutableListOf<Exercise>(
                // CHEST
                Exercise(
                    UUID.randomUUID().toString(),
                    "SUPINO RETO COM HALTERES",
                    "supino_reto_halter",
                    Muscle.CHEST,
                    Metrics.CARGA
                ),
                Exercise(
                    UUID.randomUUID().toString(),
                    "SUPINO RETO COM BARRA",
                    "supino_reto_barra",
                    Muscle.CHEST,
                    Metrics.CARGA
                ),
                Exercise(
                    UUID.randomUUID().toString(),
                    "SUPINO INCLINADO COM HALTERES",
                    "supino_inclinado_halter",
                    Muscle.CHEST,
                    Metrics.CARGA
                ),
                Exercise(
                    UUID.randomUUID().toString(),
                    "SUPINO INCLINADO COM BARRA",
                    "supino_inclinado_barra",
                    Muscle.CHEST,
                    Metrics.CARGA
                ),
                Exercise(
                    "CRUCIFIXO RETO",
                    "https://www.mundoboaforma.com.br/wp-content/uploads/2019/11/03081301-crucifixo-com-halteres.gif",
                    Muscle.CHEST,
                    Metrics.CARGA
                ),
                Exercise(
                    "VOADOR PEITORAL",
                    "https://www.blog.treinoemalta.com.br/wp-content/uploads/2023/07/Peck-Deck.gif",
                    Muscle.CHEST,
                    Metrics.CARGA
                ),
                Exercise(
                    "FLEXÃO DE BRAÇOS",
                    "https://www.hipertrofia.org/blog/wp-content/uploads/2019/12/negative-push-up.gif",
                    Muscle.CHEST,
                    Metrics.CARGA
                ),
                Exercise(
                    UUID.randomUUID().toString(),
                    "MERGULHO EM PARALELAS",
                    "dip",
                    Muscle.CHEST,
                    Metrics.CARGA
                ),
                Exercise(
                    "SUPINO MÁQUINA",
                    "https://s6.gifyu.com/images/supino-reto_na-maquina.gif",
                    Muscle.CHEST,
                    Metrics.CARGA
                ),

                // BACK
                Exercise(
                    UUID.randomUUID().toString(),
                    "PUXADOR PULLEY",
                    "puxador_pulley",
                    Muscle.BACK,
                    Metrics.CARGA
                ),
                Exercise(
                    UUID.randomUUID().toString(),
                    "BARRA FIXA",
                    "https://www.mundoboaforma.com.br/wp-content/uploads/2020/12/costas-barra-fixa-pegada-aberta-palma-para-frente-chinup.gif",
                    Muscle.BACK,
                    Metrics.CARGA
                ),
                Exercise(
                    UUID.randomUUID().toString(),
                    "LEVANTAMENTO TERRA",
                    "deadlift",
                    Muscle.BACK,
                    Metrics.CARGA
                ),
                Exercise(
                    UUID.randomUUID().toString(),
                    "REMADA CURVADA",
                    "remada_curvada",
                    Muscle.BACK,
                    Metrics.CARGA
                ),
                Exercise(
                    UUID.randomUUID().toString(),
                    "REMADA BAIXA",
                    "https://www.mundoboaforma.com.br/wp-content/uploads/2021/09/remada-sentado-com-cabos-e-triangulo-para-costas.gif",
                    Muscle.BACK,
                    Metrics.CARGA
                ),
                Exercise(
                    UUID.randomUUID().toString(),
                    "REMADA CAVALINHO",
                    "https://www.mundoboaforma.com.br/wp-content/uploads/2020/12/costas-remada-em-pe-com-barra-T.gif",
                    Muscle.BACK,
                    Metrics.CARGA
                ),
                Exercise(
                    UUID.randomUUID().toString(),
                    "REMADA NO BANCO",
                    "https://www.mundoboaforma.com.br/wp-content/uploads/2020/12/costas-remada-no-banco-inclinado-com-halteres.gif",
                    Muscle.BACK,
                    Metrics.CARGA
                ),
                Exercise(
                    UUID.randomUUID().toString(),
                    "REMADA UNILATERAL",
                    "https://static.wixstatic.com/media/2edbed_cf8feb6f79494833b887104bc358843d~mv2.gif",
                    Muscle.BACK,
                    Metrics.CARGA
                ),

                // SHOULDER
                Exercise(
                    UUID.randomUUID().toString(),
                    "LEVANTAMENTO FRONTAL",
                    "https://www.hipertrofia.org/blog/wp-content/uploads/2023/11/dumbbell-front-raise.gif",
                    Muscle.SHOULDER,
                    Metrics.CARGA
                ),
                Exercise(
                    UUID.randomUUID().toString(),
                    "LEVANTAMENTO LATERAL",
                    "levantamento_lateral",
                    Muscle.SHOULDER,
                    Metrics.CARGA
                ),
                Exercise(
                    UUID.randomUUID().toString(),
                    "DESENVOLVIMENTO MÁQUINA",
                    "https://karoldeliberato.com.br/wp-content/uploads/2023/04/image70.gif",
                    Muscle.SHOULDER,
                    Metrics.CARGA
                ),
                Exercise(
                    UUID.randomUUID().toString(),
                    "DESENVOLVIMENTO HALTERES",
                    "https://karoldeliberato.com.br/wp-content/uploads/2023/04/image36-1.gif",
                    Muscle.SHOULDER,
                    Metrics.CARGA
                ),
                Exercise(
                    UUID.randomUUID().toString(),
                    "CRUCIFIXO INVERTIDO",
                    "https://www.mundoboaforma.com.br/wp-content/uploads/2020/12/ombros-crucifixo-invertido-com-halteres.gif",
                    Muscle.SHOULDER,
                    Metrics.CARGA
                ),
                Exercise(
                    UUID.randomUUID().toString(),
                    "VOADOR INVERSO",
                    "https://www.mundoboaforma.com.br/wp-content/uploads/2020/12/ombros-voador-invertido-na-maquina.gif",
                    Muscle.SHOULDER,
                    Metrics.CARGA
                ),

                // TRICEPS
                Exercise(
                    UUID.randomUUID().toString(),
                    "TRICEPS CORDA",
                    "triceps_corda",
                    Muscle.TRICEPS,
                    Metrics.CARGA
                ),
                Exercise(
                    UUID.randomUUID().toString(),
                    "TRICEPS BARRA RETA",
                    "triceps_pulley",
                    Muscle.TRICEPS,
                    Metrics.CARGA
                ),
                Exercise(
                    UUID.randomUUID().toString(),
                    "TRICEPS FRANCES",
                    "https://www.hipertrofia.org/blog/wp-content/uploads/2023/10/dumbbell-seated-triceps-extension.gif",
                    Muscle.TRICEPS,
                    Metrics.CARGA
                ),
                Exercise(
                    UUID.randomUUID().toString(),
                    "TRICEPS TESTA",
                    "triceps_testa",
                    Muscle.TRICEPS,
                    Metrics.CARGA
                ),
                Exercise(
                    UUID.randomUUID().toString(),
                    "TRAPEZIO",
                    "https://fitnessprogramer.com/wp-content/uploads/2021/04/Dumbbell-Shrug.gif",
                    Muscle.TRICEPS,
                    Metrics.CARGA
                ),

                // BICEPS
                Exercise(
                    UUID.randomUUID().toString(),
                    "ROSCA MARTELO",
                    "rosca_martelo",
                    Muscle.BICEPS,
                    Metrics.CARGA
                ),
                Exercise(
                    UUID.randomUUID().toString(),
                    "ROSCA DIRETA COM A BARRA",
                    "rosca_direta_barra",
                    Muscle.BICEPS,
                    Metrics.CARGA
                ),
                Exercise(
                    UUID.randomUUID().toString(),
                    "ROSCA DIRETA NO PULLEY",
                    "rosca_direta_pulley",
                    Muscle.BICEPS,
                    Metrics.CARGA
                ),

                // QUADRICEPS
                Exercise(UUID.randomUUID().toString(), "AGACHAMENTO", "squat", Muscle.QUADRICEPS,
                    Metrics.CARGA),
                Exercise(
                    UUID.randomUUID().toString(),
                    "LEVANTAMENTO TERRA",
                    "deadlift",
                    Muscle.QUADRICEPS,
                    Metrics.CARGA
                ),
                Exercise(
                    UUID.randomUUID().toString(),
                    "CADEIRA EXTENSORA",
                    "https://media.tenor.com/fNeMiJuGmEcAAAAM/cadeira-extensora-extensora.gif",
                    Muscle.QUADRICEPS,
                    Metrics.CARGA
                ),
                Exercise(
                    UUID.randomUUID().toString(),
                    "AGACHAMENTO SUMÔ",
                    "https://www.mundoboaforma.com.br/wp-content/uploads/2021/09/agachamento-sumo-sem-halter.gif",
                    Muscle.QUADRICEPS,
                    Metrics.CARGA
                ),
                Exercise(
                    "LEG PRESS",
                    "https://www.inspireusafoundation.org/wp-content/uploads/2022/10/leg-press.gif",
                    Muscle.QUADRICEPS,
                    Metrics.CARGA
                ),


                // HARMSTRING
                Exercise(UUID.randomUUID().toString(), "STIFF", "stiff", Muscle.HAMSTRING,
                    Metrics.CARGA),
                Exercise(
                    UUID.randomUUID().toString(),
                    "MESA FLEXORA",
                    "mesa_flexora",
                    Muscle.HAMSTRING,
                    Metrics.CARGA
                ),
                Exercise(
                    UUID.randomUUID().toString(),
                    "BOM DIA",
                    "https://www.hipertrofia.org/blog/wp-content/uploads/2023/09/barbell-good-morning.gif",
                    Muscle.HAMSTRING,
                    Metrics.CARGA
                ),
                Exercise(
                    UUID.randomUUID().toString(),
                    "ELEVAÇÃO PÉLVICA",
                    "https://static.wixstatic.com/media/2edbed_852ea3938607497aa100eb79e600e11a~mv2.gif",
                    Muscle.HAMSTRING,
                    Metrics.CARGA
                ),
                Exercise(
                    UUID.randomUUID().toString(),
                    "EXTENSÃO DE QUADRIL",
                    "https://www.mundoboaforma.com.br/wp-content/uploads/2020/11/coice-no-cabo.gif",
                    Muscle.HAMSTRING,
                    Metrics.CARGA
                ),

                // GLUTES / CALVES
                Exercise(
                    UUID.randomUUID().toString(),
                    "PANTURRILHA EM PÉ",
                    "panturrilha_em_pe",
                    Muscle.CALVES,
                    Metrics.CARGA
                ),
                Exercise(
                    UUID.randomUUID().toString(),
                    "GÊMEOS SENTADO",
                    "gemeos_sentado",
                    Muscle.CALVES,
                    Metrics.CARGA
                ),


                // ABS
                Exercise(UUID.randomUUID().toString(), "ABDOMINAL INFRA NA PARALELA", "https://i.pinimg.com/originals/89/8b/ef/898bef76a0ef24da097dcecaf9b1a063.gif", Muscle.ABDOMINAL, Metrics.CARDIO),
                Exercise(UUID.randomUUID().toString(), "ABDOMINAL CRUNCH", "https://burnfit.io/wp-content/uploads/2023/11/CRUNCH.gif", Muscle.ABDOMINAL, Metrics.CARDIO),
                Exercise(UUID.randomUUID().toString(), "PRANCHA", "plank", Muscle.ABDOMINAL, Metrics.CARDIO),
                Exercise(
                    "ABDOMINAL INFRA",
                    "https://www.mundoboaforma.com.br/wp-content/uploads/2021/04/abdominal-no-chao-com-elevacao-de-pernas-semi-dobradas.gif",
                    Muscle.ABDOMINAL,
                    Metrics.CARGA
                ),

                // CARDIO
                Exercise(UUID.randomUUID().toString(), "CORRIDA", "running", Muscle.CARDIO, Metrics.CARDIO),
                Exercise(UUID.randomUUID().toString(), "BICICLETA", "bike", Muscle.CARDIO, Metrics.CARDIO),
                Exercise(UUID.randomUUID().toString(), "ESCADA", "stairs", Muscle.CARDIO, Metrics.CARDIO),
                Exercise(UUID.randomUUID().toString(), "NATAÇÃO", "swimming", Muscle.CARDIO, Metrics.CARDIO),
                Exercise(UUID.randomUUID().toString(), "CORDA", "https://www.mundoboaforma.com.br/wp-content/uploads/2021/04/pular-corda.gif", Muscle.CARDIO, Metrics.CARDIO),
                Exercise(UUID.randomUUID().toString(), "ARTES MARCIAIS", "fight", Muscle.CARDIO, Metrics.CARDIO),

                // OTHERS
            )
        }
    }
}