package com.apicela.training.ui.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.apicela.training.R
import com.apicela.training.adapters.WorkoutAdapter
import com.apicela.training.models.Workout
import com.apicela.training.services.WorkoutService
import com.apicela.training.ui.activitys.CreateWorkout
import com.apicela.training.utils.Codes
import kotlinx.coroutines.runBlocking

class HomeFragment : Fragment(R.layout.fragment_home) {
    lateinit var newWorkoutButton: Button
    lateinit var workoutAdapter: WorkoutAdapter
    lateinit var recyclerView: RecyclerView // Add this line
    lateinit var listOfWorkouts: List<Workout>
    private val workoutService: WorkoutService = WorkoutService()
    lateinit var fytIcon : ImageView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpVariables()
        setOnClickWorkoutButton()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        linkViewFields(view)
        return view;
    }

    private fun setUpVariables() {
        listOfWorkouts = runBlocking { workoutService.getAllWorkouts() }
        workoutAdapter = WorkoutAdapter(requireContext(), listOfWorkouts, workoutService)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = workoutAdapter
    }

    private fun setOnClickWorkoutButton() {
        newWorkoutButton.setOnClickListener {
            val intent = Intent(requireContext(), CreateWorkout::class.java)
            startActivityForResult(intent, Codes.REQUEST_CODE_CREATED)
        }
        fytIcon.setOnClickListener {
            val url = "https://github.com/apicela/FitYourTraining"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
        }
    }


    private fun linkViewFields(v: View) {
        newWorkoutButton = v.findViewById(R.id.buttonNewWorkout)
        recyclerView = v.findViewById(R.id.recyclerView)
        fytIcon = v.findViewById(R.id.fyt_icon)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Codes.REQUEST_CODE_CREATED && resultCode == Codes.RESULT_CODE_EXERCISE_CREATED) {
            this.workoutAdapter.refreshData()
        }
    }
}