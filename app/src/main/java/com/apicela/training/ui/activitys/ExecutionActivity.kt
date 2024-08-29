package com.apicela.training.ui.activitys

//class ExecutionActivity : AppCompatActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_execution)
//    }
//}

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.apicela.training.R
import com.apicela.training.adapters.ExecutionAdapter
import com.apicela.training.ui.dialogs.EditTimerDialog
import com.apicela.training.ui.dialogs.RegisterExecutionCardio
import com.apicela.training.ui.dialogs.RegisterExecutionDialog
import com.apicela.training.ui.utils.ImageHelper
import com.apicela.training.ui.utils.TimerImpl
import com.google.android.material.imageview.ShapeableImageView

class ExecutionActivity : AppCompatActivity() {

    private lateinit var executionAdapter: ExecutionAdapter
    private lateinit var timerLayout: LinearLayout
    private lateinit var recyclerViewExecutions: RecyclerView // Add this line
    private lateinit var plusButton: ImageButton
    private lateinit var backButton: Button
    private lateinit var edit: Button
    private lateinit var nameText: TextView
    private lateinit var imageExercise: ShapeableImageView
    private lateinit var exerciseId: String
    private lateinit var metric: String
    private lateinit var exerciseImage: String
    var editMode = true;
    private lateinit var timer: TimerImpl


    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("activity", "execution started")
        super.onCreate(savedInstanceState)
        bindView()
        setUpViews()
        setUpTimer()
        setUpRecyclerView()
        setUpOnClick()
    }

    private fun setUpTimer() {
        timer = TimerImpl(this, timerLayout)
    }

    private fun setUpViews() {
        exerciseId = intent.getStringExtra("exercise_id") as String
        metric = intent.getStringExtra("metric") as String
        nameText.text = intent.getStringExtra("exercise_name") as String
        exerciseImage = intent.getStringExtra("exercise_image") as String
        ImageHelper.setImage(this, imageExercise, exerciseImage, false)
    }

    private fun setUpOnClick() {
        plusButton.setOnClickListener {
            val dialog = if(metric.equals("CARGA")) RegisterExecutionDialog(exerciseId, null, this) else RegisterExecutionCardio(exerciseId, null, this);
            dialog.show(supportFragmentManager, "RegistrarExercicioDialog");
            when (dialog) {
                is RegisterExecutionDialog -> {
                    dialog.onDismissListener = {
                        executionAdapter.refreshData(exerciseId)
                    }
                }
                is RegisterExecutionCardio -> {
                    dialog.onDismissListener = {
                        executionAdapter.refreshData(exerciseId)
                    }
                }
            }


        }
        backButton.setOnClickListener {
            finish()
        }
        edit.setOnClickListener {
            executionAdapter.setEditing(editMode)
            editMode = !editMode
        }
        timerLayout.setOnClickListener{
            val dialog = EditTimerDialog()
            if (this is FragmentActivity) {
                dialog.show(this.supportFragmentManager, "RegistrarExercicioDialog")
            }
            dialog.onDismissListener = { // Configura o listener para saber da dismiss do diálogo
                val confirmDelete = dialog.confirmed
                if (confirmDelete) {
                    timer.refreshTimeValue()
                }
            }
            true
        }
    }

    private fun setUpRecyclerView() {
        executionAdapter = ExecutionAdapter(this, exerciseId)
        recyclerViewExecutions.layoutManager = LinearLayoutManager(this)
        recyclerViewExecutions.adapter = executionAdapter
    }

    private fun bindView() {
        setContentView(R.layout.activity_execution)
        backButton = findViewById(R.id.back_button)
        edit = findViewById(R.id.edit)
        recyclerViewExecutions = findViewById(R.id.recyclerViewExecutions)
        plusButton = findViewById(R.id.plus_button)
        nameText = findViewById(R.id.name)
        imageExercise = findViewById(R.id.image)
        timerLayout = findViewById(R.id.timerLayout)
    }
}

