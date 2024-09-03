package com.apicela.training.services

import com.apicela.training.data.Database
import com.apicela.training.models.Observation
import com.apicela.training.ui.activitys.HomeActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Locale

class ObservationService(private val db: Database = HomeActivity.DATABASE) {

    suspend fun addObservation(observation: Observation) {
//        Log.d("Observation", "Observation added to database + ${observation.toString()}")
        withContext(Dispatchers.IO) {
            db.observationDao().insert(observation);
        }
    }

    suspend fun update(observation: Observation) {
        withContext(Dispatchers.IO) {
            db.observationDao().update(observation);
        }
    }

    // @Pattern dd/MM/yyyy
    suspend fun getObservationByDate(date: String): Observation? {
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val dateLong = sdf.parse(date)?.time
//        Log.d("Observation", "date: ${date} dateLong: ${dateLong}")
        return withContext(Dispatchers.IO) { db.observationDao().getObservationByDate(dateLong!!) }
    }
}