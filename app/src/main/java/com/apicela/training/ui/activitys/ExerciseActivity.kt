package com.apicela.training.ui.activitys


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import android.widget.SearchView
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.apicela.training.ItemMoveCallback
import com.apicela.training.ItemTouchHelperAdapter
import com.apicela.training.R
import com.apicela.training.adapters.ExerciseAdapter
import com.apicela.training.adapters.ExerciseItemAdapter
import com.apicela.training.interfaces.ExerciseAdapterInterface
import com.apicela.training.services.ExerciseService
import com.apicela.training.utils.Codes.Companion.REQUEST_CODE_CREATED
import com.apicela.training.utils.Codes.Companion.RESULT_CODE_EXERCISE_CREATED
import kotlinx.coroutines.runBlocking


class ExerciseActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var exerciseAdapter: RecyclerView.Adapter<*>
    private lateinit var plusButton: ImageButton
    private lateinit var backButton: Button
    private lateinit var editButton: Button
    private lateinit var searchView : SearchView
    private val exerciseService: ExerciseService = ExerciseService()
    private var editMode = false;
    private var divisionId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("activity", "exercise started")
        super.onCreate(savedInstanceState)
        divisionId = intent.getStringExtra("division_id")
        bindViews()
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
        plusButton.setOnClickListener {
            if (divisionId != null) {
                val intent = Intent(this@ExerciseActivity, AddExerciseActivity::class.java)
                intent.putExtra("division_id", divisionId)
                startActivityForResult(intent, REQUEST_CODE_CREATED)
            } else {
                val intent = Intent(this@ExerciseActivity, CreateExercise::class.java)
                startActivityForResult(intent, REQUEST_CODE_CREATED)
            }
        }
        backButton.setOnClickListener {
            finish()
        }

        editButton.setOnClickListener {
            editMode = !editMode
            if (exerciseAdapter is ExerciseAdapterInterface) {
                (exerciseAdapter as ExerciseAdapterInterface).setEditing(editMode)
            }
            plusButton.visibility =
                if (plusButton.visibility == View.VISIBLE) View.GONE else View.VISIBLE
        }
    }

    private fun setUpRecyclerView() {
        exerciseAdapter = if (divisionId == null) {
            val exerciseListMap = runBlocking { exerciseService.exerciseListToMap() }
            ExerciseAdapter(this, exerciseListMap, null, exerciseService)
        } else ExerciseItemAdapter(this, divisionId)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = exerciseAdapter
        if (exerciseAdapter is ItemTouchHelperAdapter) {
            val callback = ItemMoveCallback(exerciseAdapter as ItemTouchHelperAdapter)
            val touchHelper = ItemTouchHelper(callback)
            touchHelper.attachToRecyclerView(recyclerView)
        }
    }

    private fun bindViews() {
        setContentView(R.layout.activity_exercise)
        // layouts
        plusButton = findViewById(R.id.plus_button)
        backButton = findViewById(R.id.back_button)
        editButton = findViewById(R.id.edit)
        recyclerView = findViewById(R.id.recyclerView)

        // search
        searchView = findViewById<SearchView>(R.id.searchView)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d("ExerciseActivity", "here")
        if (requestCode == REQUEST_CODE_CREATED && resultCode == RESULT_CODE_EXERCISE_CREATED) {
            if (exerciseAdapter is ExerciseAdapterInterface) {
                (exerciseAdapter as ExerciseAdapterInterface).refreshData()
            }
        }
    }

    public fun refreshExerciseAdapter(){
        if (exerciseAdapter is ExerciseAdapterInterface) {
            (exerciseAdapter as ExerciseAdapterInterface).refreshData()
        }
    }
    override fun onResume() {
        super.onResume()
        if (exerciseAdapter is ExerciseAdapterInterface) {
            (exerciseAdapter as ExerciseAdapterInterface).refreshData()
        }
    }
}