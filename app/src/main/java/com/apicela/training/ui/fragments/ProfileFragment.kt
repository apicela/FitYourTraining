package com.apicela.training.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.apicela.training.R
import com.apicela.training.models.Observation
import com.apicela.training.services.ObservationService
import com.apicela.training.ui.utils.Components
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

//
class ProfileFragment : Fragment(R.layout.fragment_profile) {
    lateinit var saveButton: Button
    lateinit var buttonDecrementDate: ImageButton
    lateinit var buttonIncrementDate: ImageButton
    lateinit var editTextObservation: EditText
    lateinit var editTextDate: EditText
    lateinit var observationService: ObservationService

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        linkViewFields(view)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpVariables()
        val todayDate = Instant.now().atZone(ZoneId.systemDefault()).toLocalDateTime().format(
            DateTimeFormatter.ofPattern("dd/MM/yyyy")
        )
        setUpDateInfo(todayDate)
        setOnClick()
    }

    private fun setUpVariables() {
        observationService = ObservationService()
    }

    // @require date as dd/MM/yyyy
    fun setUpDateInfo(date: String) {
        editTextDate.setText(date)
        val observationFromDB = runBlocking { observationService.getObservationByDate(date) }
        Log.d("ProfileFragment", "observationFromDB: ${observationFromDB.toString()}")
        val observationText = observationFromDB?.observation ?: ""
        editTextObservation.setText(observationText)
    }

    private fun setOnClick() {
        // saveButton
        saveButton.setOnClickListener {
            val observationText = editTextObservation.text.toString()
            val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val dateString = editTextDate.text.toString()

            val observation = Observation(dateFormat.parse(dateString)!!, observationText)
//            Log.d("ProfileFragment", "date from editText: ${Date(editTextDate.text.toString())} + editText.text : ${editTextDate.text.toString()}")
            CoroutineScope(Dispatchers.IO).launch {
                observationService.addObservation(observation)
            }
            Toast.makeText(context, "Salvo com sucesso!", Toast.LENGTH_SHORT).show()
        }

        buttonIncrementDate.setOnClickListener {
            val newDate = updateDate(editTextDate.text.toString(), 1)
            editTextDate.setText(newDate)
            setUpDateInfo(newDate)
        }
        buttonDecrementDate.setOnClickListener {
            val newDate = updateDate(editTextDate.text.toString(), -1)
            editTextDate.setText(newDate)
            setUpDateInfo(newDate)
        }

        // edit text date
        editTextDate.setOnClickListener {
            Components.showDatePicker(editTextDate, requireContext())
            editTextDate.requestFocus() // Request focus after showing the DatePicker
        }
    }


    private fun linkViewFields(view: View) {
        // buttons
        saveButton = view.findViewById(R.id.buttonSave)
        buttonDecrementDate = view.findViewById(R.id.buttonDecrementDate)
        buttonIncrementDate = view.findViewById(R.id.buttonIncrementDate)

        // texts
        editTextObservation = view.findViewById(R.id.editTextObservation)
        editTextDate = view.findViewById(R.id.editTextDate)

    }

    fun updateDate(dateAsString: String, days: Int): String {
        val currentDate = LocalDate.parse(dateAsString, DateTimeFormatter.ofPattern("dd/MM/yyyy"))
        val updatedDate = currentDate.plusDays(days.toLong()).format(
            DateTimeFormatter.ofPattern("dd/MM/yyyy")
        )
        return updatedDate;
    }
}
