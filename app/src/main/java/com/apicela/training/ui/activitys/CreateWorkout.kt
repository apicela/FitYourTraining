package com.apicela.training.ui.activitys

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.apicela.training.R
import com.apicela.training.services.WorkoutService
import com.apicela.training.utils.Codes
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CreateWorkout : AppCompatActivity() {
    private lateinit var backButton: Button
    private lateinit var concludeButton: Button
    private lateinit var workoutName: EditText
    private lateinit var descricaoText: EditText

    private val workoutService: WorkoutService = WorkoutService()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setUpViews()
        setUpOnClick()
    }

    private fun setUpOnClick() {
        concludeButton.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                createWorkout()
                val resultIntent = Intent()
                setResult(Codes.RESULT_CODE_EXERCISE_CREATED, resultIntent)
                finish()
            }
        }

        backButton.setOnClickListener {
            finish()
        }
    }

    private suspend fun createWorkout() {
        var image = listOf("simpsons", "simpsons2", "bobsponja").random()
        workoutService.addWorkout(
            workoutName.text.toString(),
            descricaoText.text.toString(), image
        )
    }

    private fun setUpViews() {
        setContentView(R.layout.activity_create_workout)
        backButton = findViewById(R.id.back_button)
        concludeButton = findViewById(R.id.concludeButton)
        workoutName = findViewById(R.id.workoutName)
        descricaoText = findViewById(R.id.descricaoText)
    }

}