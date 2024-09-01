package com.apicela.training.ui.activitys

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.apicela.training.R
import com.apicela.training.models.Exercise
import com.apicela.training.models.extra.Metrics
import com.apicela.training.models.extra.Muscle
import com.apicela.training.services.ExerciseService
import com.apicela.training.utils.UtilsComponents
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class CreateExercise : AppCompatActivity() {
    private lateinit var backButton: Button
    private lateinit var concludeButton: Button
    private lateinit var exerciseService: ExerciseService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val exerciseId = intent.getStringExtra("exerciseId") as String?
        setContentView(R.layout.activity_create_exercise)
        val exerciseName: EditText = findViewById(R.id.exerciseNameText)
        val imageUrl: EditText = findViewById(R.id.imageUrlText)
        val muscleTypeSpinner: Spinner = findViewById(R.id.muscleTypeSpinner)
        val metricTypeSpinner: Spinner = findViewById(R.id.metricTypeSpinner)

        exerciseService = ExerciseService()
        backButton = findViewById(R.id.back_button)
        concludeButton = findViewById(R.id.concludeButton)


        val items = Muscle.getAsListPTBR()
        val metricItems = Metrics.getAsListPTBR()

        val adapter = ArrayAdapter(this, R.layout.transparent_layout, items)
        adapter.setDropDownViewResource(R.layout.dropdown_muscle_type)
        muscleTypeSpinner.adapter = adapter

        val metricsAdapter = ArrayAdapter(this, R.layout.transparent_layout, metricItems)
        metricsAdapter.setDropDownViewResource(R.layout.dropdown_muscle_type)
        metricTypeSpinner.adapter = metricsAdapter
        var exerciseItem: Exercise? = null;
        if (exerciseId != null) {
            exerciseItem = runBlocking { exerciseService.getExerciseById(exerciseId) }
            exerciseName.setText("${exerciseItem.name}")
            imageUrl.setText("${exerciseItem.image}")

            val positionMuscle = items.indexOf(exerciseItem.muscleType.ptbr)
            muscleTypeSpinner.setSelection(positionMuscle)

            val positionMetrics = metricItems.indexOf(exerciseItem.metricType.ptbr)
            metricTypeSpinner.setSelection(positionMetrics)
        }

        concludeButton.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                val muscle = (UtilsComponents.getSpinnerSelectedItem(muscleTypeSpinner))
                val metric = (UtilsComponents.getSpinnerSelectedItem(metricTypeSpinner))
                if (exerciseId == null) {
                    exerciseService.addExerciseToDatabase(
                        Exercise(
                            exerciseName.text.toString(),
                            imageUrl.text.toString(),
                            Muscle.getMusclePTBRtoENG(muscle)!!,
                            Metrics.getMetricPTBRtoENG(metric)!!
                        )
                    )
                } else {
                    if (exerciseItem != null) {
                        exerciseItem.name = exerciseName.text.toString()
                        exerciseItem.image = imageUrl.text.toString()
                        exerciseItem.muscleType = Muscle.getMusclePTBRtoENG(muscle)!!
                        exerciseItem.metricType = Metrics.getMetricPTBRtoENG(metric)!!
                    }
                    exerciseService.updateExercise(exerciseItem!!)
                }
            }
            Toast.makeText(this, "Exercício inserido!", Toast.LENGTH_SHORT).show()
            finish()
        }

        backButton.setOnClickListener {
            finish()
        }
    }


}