package com.apicela.training.ui.activitys

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.apicela.training.R
import com.apicela.training.adapters.DivisionAdapter
import com.apicela.training.services.WorkoutService
import com.apicela.training.utils.Codes
import kotlinx.coroutines.runBlocking

class DivisionActivity : AppCompatActivity() {
    private lateinit var backButton: Button
    private lateinit var editButton: Button
    private lateinit var plusButton: ImageButton
    private lateinit var descriptionText: TextView
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: DivisionAdapter

    private val workoutService: WorkoutService = WorkoutService()
    private var editMode = false;
    private var workoutId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("activity", "division started")
        super.onCreate(savedInstanceState)
        bindViews()
        initializeVariables()
        setUpRecyclerView()
        setUpOnClick()
    }

    private fun setUpOnClick() {
        backButton.setOnClickListener {
            finish()
        }

        editButton.setOnClickListener {
            editMode = !editMode
            adapter.setEditing(editMode)
            plusButton.visibility =
                if (plusButton.visibility == View.VISIBLE) View.GONE else View.VISIBLE
        }

        plusButton.setOnClickListener {
            val intent = Intent(this@DivisionActivity, CreateDivision::class.java)
            intent.putExtra("workout_id", workoutId)
            startActivityForResult(intent, Codes.REQUEST_CODE_CREATED)
        }
    }

    private fun initializeVariables() {
        workoutId = intent.getStringExtra("workout_id")
        val workoutDescription =
            runBlocking { workoutService.getWorkoutById(workoutId!!).description }
        descriptionText.text = workoutDescription

    }

    private fun setUpRecyclerView() {
        adapter = DivisionAdapter(this, workoutId!!)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
    }

    private fun bindViews() {
        setContentView(R.layout.activity_division)
        plusButton = findViewById(R.id.plus_button)
        backButton = findViewById(R.id.back_button)
        editButton = findViewById(R.id.edit)
        descriptionText = findViewById(R.id.description_text)
        recyclerView = findViewById(R.id.recyclerView)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Codes.REQUEST_CODE_CREATED) {
            recreate() // Isso reiniciará a Activity
        }
    }
}