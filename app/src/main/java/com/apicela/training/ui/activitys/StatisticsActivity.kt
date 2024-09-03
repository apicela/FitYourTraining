package com.apicela.training.ui.activitys

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import com.apicela.training.R
import com.apicela.training.models.Exercise
import com.apicela.training.models.extra.ExecutionInfo
import com.apicela.training.models.extra.ExecutionRaw
import com.apicela.training.services.ExerciseService
import com.apicela.training.utils.UtilsComponents
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.Chart
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.DecimalFormat

class StatisticsActivity : AppCompatActivity() {
    private lateinit var backButton: Button
    private lateinit var exerciseAutoComplete: AutoCompleteTextView
    private lateinit var chartWeightBar: BarChart
    private lateinit var chartRepetitionsBar: BarChart
    private lateinit var chartWeightLine: LineChart
    private lateinit var chartRepetitionsLine: LineChart
    private lateinit var exerciseItems: List<Exercise>
    private lateinit var spinnerGraphType: Spinner
    private lateinit var spinnerPastMonths: Spinner
    private lateinit var spinnerFilter: Spinner
    private lateinit var arrayAdapter: ArrayAdapter<*>
    private lateinit var itemsList: List<ExecutionInfo>
    private lateinit var selectedExercise: Exercise
    private val exerciseService: ExerciseService = ExerciseService()
    private val GRAPH_TYPES = listOf("BARRAS", "LINHAS")
    private val PAST_MONTHS = listOf("3 MESES", "6 MESES", "9 MESES", "12 MESES")
    private val FILTER_TYPE = listOf("MODA", "MÉDIA", "MAX")
    private var  exerciseId : String?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        exerciseId = intent.getStringExtra("exercise_id")
        bindViews()
        setUpVariables(this)
        setUpOnChange()
    }

    private fun setUp() {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val chartType = UtilsComponents.getSpinnerSelectedItem(spinnerGraphType)
                itemsList = withContext(Dispatchers.IO) {
                    exerciseService.getExerciseInfoPastMonths(selectedExercise.id, UtilsComponents.getSpinnerSelectedItem(spinnerPastMonths).split(" ")[0].toInt())
                }
                updateGraphs(itemsList, chartType, UtilsComponents.getSpinnerSelectedItem(spinnerFilter))
            } catch (e: Exception) {
                Log.e("Statistics", "Error fetching data", e)
            }
        }
    }


    private fun setUpOnChange() {
        backButton.setOnClickListener {
            finish()
        }
            exerciseAutoComplete.setOnItemClickListener { parent, view, position, id ->
                Log.d("Statistics", " parent: ${parent.getItemAtPosition(position)}")
                selectedExercise = exerciseItems.find{ it.name == parent.getItemAtPosition(position) }!!
                val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(exerciseAutoComplete.windowToken, 0)

                Log.d("Statistics", "exercise : $selectedExercise")
                CoroutineScope(Dispatchers.Main).launch {
                    try {
                        val chartType = UtilsComponents.getSpinnerSelectedItem(spinnerGraphType)
                        itemsList = withContext(Dispatchers.IO) {
                            exerciseService.getExerciseInfoPastMonths(selectedExercise.id, UtilsComponents.getSpinnerSelectedItem(spinnerPastMonths).split(" ")[0].toInt())
                        }
                        updateGraphs(itemsList, chartType, UtilsComponents.getSpinnerSelectedItem(spinnerFilter))
                    } catch (e: Exception) {
                        // Lidar com exceções, se necessário
                        Log.e("Statistics", "Error fetching data", e)
                    }
                }
        }


        exerciseAutoComplete.setOnClickListener {
            exerciseAutoComplete.setText("")
        }
        spinnerPastMonths.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                Log.d("Statistics", "spinnerPastMonthsClicked")
                if (::selectedExercise.isInitialized) {
                    val selectedItem = parent.getItemAtPosition(position).toString()
                    val selectedMonth = selectedItem.split(" ")[0].toInt()
                    CoroutineScope(Dispatchers.Main).launch {
                        itemsList = exerciseService.getExerciseInfoPastMonths(
                            selectedExercise.id,
                            selectedMonth
                        )
                        updateGraphs(
                            itemsList,
                            UtilsComponents.getSpinnerSelectedItem(spinnerGraphType),
                            UtilsComponents.getSpinnerSelectedItem(spinnerFilter)
                        )
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        }

        spinnerGraphType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                val selectedItem = parent.getItemAtPosition(position).toString()
                if (::itemsList.isInitialized) {
                    updateGraphs(itemsList, selectedItem, UtilsComponents.getSpinnerSelectedItem(spinnerFilter))
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        }

        spinnerFilter.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                val selectedItem = parent.getItemAtPosition(position).toString()
                if (::itemsList.isInitialized) {
                    updateGraphs(itemsList, UtilsComponents.getSpinnerSelectedItem(spinnerGraphType), selectedItem)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        }

    }

    private fun updateGraphs(itemsList: List<ExecutionInfo>, chartType: String, filter : String) {
        Log.d("Statistics", "itemsList: ${itemsList}")
        val listWithFilter: List<ExecutionRaw> = itemsList.mapNotNull { item ->
            when (filter) {
                "MODA" -> item.convertToModeOrMaxWeight()
                "MAX" -> item.convertToMaxWeight()
                "MÉDIA" -> item.convertToAverage()
                else -> null
            }
        }
        Log.d("Statistics", "listWithFilter : $listWithFilter")

        if (chartType == "BARRAS") {
            Log.d("Statistics", "BARRAS")
            setBarChartAsVisible(true)
            updateBarChart(listWithFilter)
        } else if (chartType == "LINHAS") {
            Log.d("Statistics", "LINHAS")
            setBarChartAsVisible(false)
            updateLineChart(listWithFilter)
      }
    }

    private fun setBarChartAsVisible(barIsVisible: Boolean) {
        if (barIsVisible) {
            // bar chart visible
            chartWeightBar.visibility = View.VISIBLE
            chartRepetitionsBar.visibility = View.VISIBLE
            // line chart gone
            chartWeightLine.visibility = View.INVISIBLE
            chartRepetitionsLine.visibility = View.INVISIBLE
        } else {
            // bar chart gone
            chartWeightBar.visibility = View.INVISIBLE
            chartRepetitionsBar.visibility = View.INVISIBLE
            // line chart visible
            chartWeightLine.visibility = View.VISIBLE
            chartRepetitionsLine.visibility = View.VISIBLE
        }


    }

    private fun updateLineChart(itemsList: List<ExecutionRaw>) {
        val entriesWeight = mutableListOf<Entry>()
        val entriesRepetitions = mutableListOf<Entry>()
        itemsList.forEach {
            var month = it.month.split("-")[1].toFloat()
            if(month < 10) month = month%10;
            Log.d("Statistics", "month: ${month}")
            entriesWeight.add(Entry(month.toFloat(), it.kg))
            entriesRepetitions.add(Entry(month.toFloat(), it.repetitions))
        }
        Log.d("Statistics", "entries weight: ${entriesWeight}")
        Log.d("Statistics", "entries reps: ${entriesRepetitions}")

        val weightLineDataSet = LineDataSet(entriesWeight, "MESES x KG")
        val repetitionsLineDataSet = LineDataSet(entriesRepetitions, "MESES x REPETIÇÕES")

        configureLineDataSet(weightLineDataSet)
        configureLineDataSet(repetitionsLineDataSet)

        val weightData = LineData(weightLineDataSet)
        val repetitionsData = LineData(repetitionsLineDataSet)

        chartWeightLine.data = weightData
        chartRepetitionsLine.data = repetitionsData

        configureChartStyle(chartWeightLine)
        configureChartStyle(chartRepetitionsLine)

        configureXandYaxis(chartWeightLine.xAxis, chartWeightLine.axisLeft)
        configureXandYaxis(chartRepetitionsLine.xAxis, chartRepetitionsLine.axisLeft)

        chartWeightLine.axisRight.isEnabled = false
        chartRepetitionsLine.axisRight.isEnabled = false

        runOnUiThread {
            chartRepetitionsLine.animateX(500)
            chartWeightLine.animateX(500)

            chartRepetitionsLine.invalidate()
            chartWeightLine.invalidate()
        }

    }

    private fun configureLineDataSet(lineDataSet: LineDataSet) {
        lineDataSet.color = resources.getColor(R.color.main_color, theme)
        lineDataSet.valueTextColor = Color.WHITE
        lineDataSet.valueTextSize = 16f
        lineDataSet.setCircleColor(Color.BLACK)
        lineDataSet.circleRadius = 5f
        lineDataSet.lineWidth = 3f
        lineDataSet.setDrawFilled(true)
        lineDataSet.fillColor = resources.getColor(R.color.main_color, theme)
        lineDataSet.fillAlpha = 90
        lineDataSet.valueFormatter = object : ValueFormatter() {
            private val format = DecimalFormat("###.##") // Define o formato para 2 casas decimais

            override fun getFormattedValue(value: Float): String {
                return format.format(value)
            }
        }
    }

    private fun updateBarChart(itemsList: List<ExecutionRaw>) {
        val entriesWeight = mutableListOf<BarEntry>()
        val entriesRepetitions = mutableListOf<BarEntry>()
        itemsList.forEach {
            var month = it.month.split("-")[1].toFloat()
            if(month < 10) month = month%10;
            entriesWeight.add(BarEntry(month.toFloat(), it.kg))
            entriesRepetitions.add(BarEntry(month.toFloat(), it.repetitions))
        }
        Log.d("Statistics", "weight: $entriesWeight")
        Log.d("Statistics", "reps: $entriesRepetitions")
        val weightBarDataSet = BarDataSet(entriesWeight, "MESES x KG")
        val repetitionsBarDataSet = BarDataSet(entriesRepetitions, "MESES x REPETIÇÕES")
        
        configureBarDataSet(weightBarDataSet)
        configureBarDataSet(repetitionsBarDataSet)

        val weightBarData = BarData(weightBarDataSet)
        val repetitionsBarData = BarData(repetitionsBarDataSet)
        weightBarData.barWidth = 0.9f
        repetitionsBarData.barWidth = 0.9f
        chartWeightBar.data = weightBarData
        chartRepetitionsBar.data = repetitionsBarData

        configureChartStyle(chartWeightBar)
        configureChartStyle(chartRepetitionsBar)

        configureXandYaxis(chartWeightBar.xAxis, chartWeightBar.axisLeft)
        configureXandYaxis(chartRepetitionsBar.xAxis, chartRepetitionsBar.axisLeft)

        chartWeightBar.axisRight.isEnabled = false
        chartRepetitionsBar.axisRight.isEnabled = false

        runOnUiThread {
            chartRepetitionsBar.animateY(500)
            chartWeightBar.animateY(500)

            chartRepetitionsBar.invalidate()
            chartWeightBar.invalidate()
        }
    }

    private fun configureChartStyle(chart: Chart<*>) {
        when (chart) {
            is BarChart, is LineChart -> {
                (chart as? BarChart ?: chart as? LineChart)?.apply {
                    setBackgroundColor(resources.getColor(R.color.main_background, theme))
                    setDrawGridBackground(false)
                    setTouchEnabled(true)
                    setPinchZoom(true)
                    chart.description.isEnabled = false
                    chart.legend.isEnabled  = false
//                    chart.legend.textSize = 18f
//                    chart.legend.textColor = Color.WHITE
//                    chart.legend.verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
//                    chart.legend.horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
                }
            }
        }
        if(chart is BarChart){
            chart.setDrawBarShadow(false)
            chart.setFitBars(true)
        }
    }

    private fun configureBarDataSet(barDataSet: BarDataSet) {
        barDataSet.color = Color.YELLOW
        barDataSet.valueTextColor = Color.WHITE
        barDataSet.valueTextSize = 22f
        barDataSet.setDrawValues(true)
        barDataSet.barBorderWidth = 1f
        barDataSet.barShadowColor = Color.DKGRAY
        barDataSet.valueFormatter = object : ValueFormatter() {
            private val format = DecimalFormat("###.##") // Define o formato para 2 casas decimais

            override fun getFormattedValue(value: Float): String {
                return format.format(value)
            }
        }
        barDataSet.setGradientColor(
            resources.getColor(R.color.main_color, theme),
            resources.getColor(R.color.light_yellow, theme)
        ) // Gradiente
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
//        leftAxis?.valueFormatter = object : ValueFormatter() {
//            override fun getFormattedValue(value: Float): String {
//                return "$value kg" // Adicione seu texto de legenda aqui
//            }
//        }
    }


    private fun setUpVariables(context: Context) {

        val graphTypeAdapter = ArrayAdapter(this, R.layout.transparent_layout_centered, GRAPH_TYPES)
        graphTypeAdapter.setDropDownViewResource(R.layout.dropdown_muscle_type)
        spinnerGraphType.adapter = graphTypeAdapter

        val pastMonthsAdapter =
            ArrayAdapter(this, R.layout.transparent_layout_centered, PAST_MONTHS)
        pastMonthsAdapter.setDropDownViewResource(R.layout.dropdown_muscle_type)
        spinnerPastMonths.adapter = pastMonthsAdapter

        val filterAdapter =
            ArrayAdapter(this, R.layout.transparent_layout_centered, FILTER_TYPE)
        filterAdapter.setDropDownViewResource(R.layout.dropdown_muscle_type)
        spinnerFilter.adapter = filterAdapter
        CoroutineScope(Dispatchers.IO).launch {
            exerciseItems = exerciseService.getAllExercises()
            selectedExercise = if(exerciseId == null){
                exerciseItems[0]
            } else {
                exerciseService.getExerciseById(exerciseId!!)
            }
            withContext(Dispatchers.Main) {
                arrayAdapter =
                    ArrayAdapter(context, R.layout.auto_complete, exerciseItems.map { it.name })
                exerciseAutoComplete.setAdapter(arrayAdapter)
            }
            exerciseAutoComplete.setText(selectedExercise.name)
            setUp()
        }

    }


    private fun bindViews() {
        setContentView(R.layout.activity_statistics)
        backButton = findViewById(R.id.back_button)
        exerciseAutoComplete = findViewById(R.id.exerciseAutoComplete)
        chartWeightBar = findViewById(R.id.graph_weight_barchart)
        chartRepetitionsBar = findViewById(R.id.graph_repetitions_barchart)
        chartWeightLine = findViewById(R.id.graph_weight_linechart)
        chartRepetitionsLine = findViewById(R.id.graph_repetitions_linechart)
        spinnerGraphType = findViewById(R.id.spinnerGraphType)
        spinnerPastMonths = findViewById(R.id.spinnerPastMonths)
        spinnerFilter = findViewById(R.id.spinnerFilter)
    }
}