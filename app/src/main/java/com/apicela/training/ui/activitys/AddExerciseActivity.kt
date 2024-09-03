package com.apicela.training.ui.activitys


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.SearchView
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
    private lateinit var searchView: SearchView
    private lateinit var exerciseListMap: Map<String, List<Exercise>>
    private lateinit var division: Division
    private var checkedItems: Int = 0
    private val exerciseService = ExerciseService()

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("activity", "addExerciseActivity called")
        super.onCreate(savedInstanceState)
        bindViews()
        val divisionId = intent.getStringExtra("division_id")

        CoroutineScope(Dispatchers.IO).launch {
            division = exerciseService.getDivision(divisionId)!!
        }
        runBlocking {
            exerciseListMap = exerciseService.exerciseListToMap()
        }

        setUpRecyclerView()
        setUpSearch()
        setUpOnClick()


    }

    private fun setUpSearch() {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(filter: String): Boolean {
                return false
            }

            override fun onQueryTextChange(filter: String): Boolean {
                if (exerciseAdapter is ExerciseAdapter) {
                    (exerciseAdapter as ExerciseAdapter).filterList(filter)
                }
                return true
            }
        })
        val searchEditText = searchView.findViewById<View>(
            searchView.context.resources.getIdentifier(
                "android:id/search_src_text",
                null,
                null
            )
        ) as EditText
        searchEditText.setHintTextColor(resources.getColor(R.color.white)) // Replace with desired color
        searchEditText.setTextColor(resources.getColor(R.color.white))

    }

    private fun setUpOnClick() {
        backButton.setOnClickListener {
            finish()
        }
        addExerciseToWorkoutButton.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                if (division != null) {
                    val checkedItems = exerciseAdapter.getSelectedExercises().map { it.id }
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
        searchView = findViewById<SearchView>(R.id.searchView)
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