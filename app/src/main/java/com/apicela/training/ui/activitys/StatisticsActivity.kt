package com.apicela.training.ui.activitys

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.apicela.training.R
import com.apicela.training.models.Exercise
import com.apicela.training.services.ExerciseService
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.invoke
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class StatisticsActivity : AppCompatActivity() {
    private lateinit var backButton: Button
    private lateinit var exerciseAutoComplete: AutoCompleteTextView
    private lateinit var chartWeight: BarChart
    private lateinit var chartRepetitions: LineChart
    private lateinit var exerciseItems: List<Exercise>
    private lateinit var arrayAdapter: ArrayAdapter<*>
    private val exerciseService: ExerciseService = ExerciseService()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindViews()
        setUpVariables(this)
        setUpOnChangeAutoComplete()
    }

    private fun setUpOnChangeAutoComplete() {
        exerciseAutoComplete.setOnItemClickListener { parent, view, position, id ->
                val selectedItem = exerciseItems.get(position)
            CoroutineScope(Dispatchers.Main).launch {
                try {
                    // Fazer a chamada de rede na thread de fundo
                    val result = withContext(Dispatchers.IO) {
                        exerciseService.getModeOfKgForLastSixMonths(selectedItem.id)
                    }
                    // Atualizar a UI com o resultado na thread principal
                } catch (e: Exception) {
                    // Lidar com exceções, se necessário
                    Log.e("Statistics", "Error fetching data", e)
                }}
//                updateGraphs(selectedItem, chartWeight)
//                updateGraphs(selectedItem, chartRepetitions)

        }
    }


    private fun updateGraphs(selectedItem: Exercise, barChart: BarChart) {
        Log.d("Statistics", "${selectedItem}")

        // Criação dos dados
        val entries = mutableListOf<BarEntry>()
        entries.add(BarEntry(4f, 15f))
        entries.add(BarEntry(5f, 18f))
        entries.add(BarEntry(6f, 20f))
        entries.add(BarEntry(7f, 22f))
        entries.add(BarEntry(8f, 22f))
        entries.add(BarEntry(9f, 24f))

        // Criação do DataSet
        val barDataSet = BarDataSet(entries, "Exemplo de Gráfico de Barras")
        barDataSet.color = Color.BLUE
        barDataSet.valueTextColor = Color.WHITE
        barDataSet.valueTextSize = 16f
        barDataSet.setDrawValues(true)
        barDataSet.barBorderWidth = 1f
        barDataSet.barShadowColor = Color.DKGRAY
        barDataSet.setGradientColor(resources.getColor(R.color.main_color, theme),
            resources.getColor(R.color.light_yellow, theme)) // Gradiente

        // Configurando o BarData
        val barData = BarData(barDataSet)
        barData.barWidth = 0.9f // Largura das barras
        barChart.data = barData

        // Estilo do gráfico
        barChart.setBackgroundColor(resources.getColor(R.color.main_background, theme))
        barChart.setDrawGridBackground(false)
        barChart.description.isEnabled = false
        barChart.setTouchEnabled(true)
        barChart.setPinchZoom(true)
        barChart.setDrawBarShadow(false)
        barChart.setFitBars(true)

        val xAxis = barChart.xAxis
        val leftAxis = barChart.axisLeft

        configureXandYaxis(xAxis, leftAxis)

        // Desabilita o eixo Y direito
        barChart.axisRight.isEnabled = false

        // Animação do gráfico
        barChart.animateY(500)

        // Atualiza o gráfico
        barChart.invalidate()
    }



    private fun updateGraphs(selectedItem: Exercise, barChart: LineChart) {
        Log.d("Statistics", "${selectedItem}")

        // Criação dos dados
        val entries = mutableListOf<Entry>()

        entries.add(Entry(4f, 15f))
        entries.add(Entry(5f, 18f))
        entries.add(Entry(6f, 20f))
        entries.add(Entry(7f, 22f))
        entries.add(Entry(8f, 22f))
        entries.add(Entry(9f, 24f))


        // Criação do DataSet
        val lineDataSet = LineDataSet(entries, "Exemplo de Gráfico de Linhas")
        lineDataSet.color = resources.getColor(R.color.main_color, theme)
        lineDataSet.valueTextColor = Color.WHITE
        lineDataSet.valueTextSize = 16f
        lineDataSet.setCircleColor(Color.BLACK)
        lineDataSet.circleRadius = 5f
        lineDataSet.lineWidth = 3f
        lineDataSet.setDrawFilled(true)
        lineDataSet.fillColor = resources.getColor(R.color.main_color, theme)
        lineDataSet.fillAlpha = 90

        // Configurando o LineData
        val lineData = LineData(lineDataSet)
        barChart.data = lineData

        // Estilo do gráfico
        barChart.description.isEnabled = false
        barChart.setBackgroundColor(resources.getColor(R.color.main_background, theme))
        barChart.setDrawGridBackground(false)
        barChart.setTouchEnabled(true)
        barChart.setPinchZoom(true)

        // Configurações do eixo X
        val xAxis = barChart.xAxis
        val leftAxis = barChart.axisLeft

        configureXandYaxis(xAxis, leftAxis)

        // Desabilita o eixo Y direito
        barChart.axisRight.isEnabled = false
        // Animação do gráfico
        barChart.animateX(500)

        // Atualiza o gráfico
        barChart.invalidate()
    }

    private fun configureXandYaxis(xAxis: XAxis?, leftAxis: YAxis?) {
        // x
        xAxis?.position = XAxis.XAxisPosition.BOTTOM
        xAxis?.setDrawGridLines(false)
        xAxis?.textColor = Color.WHITE
        xAxis?.textSize = 12f
        xAxis?.granularity = 1f  // Define o espaçamento mínimo de 1 unidade no eixo X

        // y
        leftAxis?.setDrawGridLines(true)
        leftAxis?.gridColor = Color.GRAY
        leftAxis?.textColor = Color.WHITE
        leftAxis?.textSize = 12f
    }


    private fun setUpVariables(context: Context) {
        CoroutineScope(Dispatchers.IO).launch {
            exerciseItems = exerciseService.getAllExercises()
            withContext(Dispatchers.Main) {
                arrayAdapter = ArrayAdapter(context, R.layout.auto_complete, exerciseItems.map{ it.name})
                exerciseAutoComplete.setAdapter(arrayAdapter)
               // arrayAdapter.setDropDownViewResource(R.layout.dropdown_muscle_type)

            }
        }
    }


    private fun bindViews() {
        setContentView(R.layout.activity_statistics)
        backButton = findViewById(R.id.back_button)
        exerciseAutoComplete = findViewById(R.id.exerciseAutoComplete)
        chartWeight = findViewById(R.id.graph_weight)
        chartRepetitions = findViewById(R.id.graph_repetitions)

    }
}