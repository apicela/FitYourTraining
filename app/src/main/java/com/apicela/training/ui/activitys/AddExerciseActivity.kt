package com.apicela.training.ui.activitys


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.apicela.training.R
import com.apicela.training.adapters.ExerciseAdapter
import com.apicela.training.interfaces.OnExerciseCheckedChangeListener
import com.apicela.training.models.Division
import com.apicela.training.models.Exercise
import com.apicela.training.services.ExerciseService
import com.apicela.training.utils.Codes.Companion.REQUEST_CODE_CREATED
import com.apicela.training.utils.Codes.Companion.RESULT_CODE_EXERCISE_CREATED
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


class AddExerciseActivity : AppCompatActivity(), OnExerciseCheckedChangeListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var exerciseAdapter: ExerciseAdapter
    private lateinit var backButton: Button
    private lateinit var addExerciseToWorkoutButton: AppCompatButton
    private lateinit var exerciseListMap: Map<String, List<Exercise>>
    private lateinit var division: Division
    private var checkedItems: Int = 0
    private val exerciseService = ExerciseService()

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("activity", "addExerciseActivity called")
        super.onCreate(savedInstanceState)
        bindViews()
        val division_id = intent.getStringExtra("division_id")

        CoroutineScope(Dispatchers.IO).launch {
            division = exerciseService.getDivision(division_id)!!
        }
        runBlocking {
            exerciseListMap = exerciseService.exerciseListToMap()
        }

        setUpRecyclerView()
        setUpOnClick()


    }

    private fun setUpOnClick() {
        backButton.setOnClickListener {
            finish()
        }
        addExerciseToWorkoutButton.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                if (division != null) {
                    val checkedItems = exerciseAdapter.getSelectedExercises()
                    val newListExercises = (division.listOfExercises + checkedItems).distinct()
                    division.listOfExercises = newListExercises
                    exerciseService.divisionService.updateDivisionObject(division)
                }
                val resultIntent = Intent()
                setResult(RESULT_CODE_EXERCISE_CREATED, resultIntent)
                finish()
            }

        }
    }

    private fun setUpRecyclerView() {
        exerciseAdapter = ExerciseAdapter(this, exerciseListMap, null, exerciseService, this)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = exerciseAdapter
    }

    private fun bindViews() {
        setContentView(R.layout.activity_add_exercise)
        backButton = findViewById(R.id.back_button)
        addExerciseToWorkoutButton = findViewById(R.id.add_exercise_to_workout)
        recyclerView = findViewById(R.id.recyclerView)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_CREATED && resultCode == RESULT_CODE_EXERCISE_CREATED) {
            recreate() // Isso reiniciará a Activity
        }
    }

    override fun onCheckedItemCountChanged(count: Int) {
        checkedItems = count
        addExerciseToWorkoutButton.text =
            "Adicionar exercícios (${checkedItems})"
    }
}