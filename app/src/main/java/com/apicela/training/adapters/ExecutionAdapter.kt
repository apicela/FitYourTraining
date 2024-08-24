package com.apicela.training.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.apicela.training.R
import com.apicela.training.models.Execution
import com.apicela.training.models.Observation
import com.apicela.training.services.ExecutionService
import com.apicela.training.services.ObservationService
import com.apicela.training.ui.dialogs.RegisterExecutionDialog
import com.apicela.training.ui.utils.Components
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


class ExecutionAdapter(
    private val context: Context,
    private val exerciseId: String
) :
    RecyclerView.Adapter<ExecutionAdapter.ExecutionViewHolder>() {
    val executionService = ExecutionService()
    var executionMap: Map<String, List<Execution>> = executionService.executionListToMap(exerciseId)
    private var isEditing = false
    private val observationService = ObservationService()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExecutionViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.date_linear_layout, parent, false)
        return ExecutionViewHolder(view)
    }

    override fun onBindViewHolder(holder: ExecutionViewHolder, position: Int) {
//        Log.d("Execution", "onBind called position: $position")
        val key = executionMap.keys.elementAt(position)
        holder.date.text = key
        var observation: Observation?
        CoroutineScope(Dispatchers.IO).launch {
            observation = observationService.getObservationByDate(key)
            if (observation != null) {
                holder.observationImage.visibility = View.VISIBLE
                setOnClickObservation(holder.observationImage, observation!!.observation)
            }
        }

        val executions = executionMap.getValue(key)
        holder.linearLayoutExecutions.removeAllViews()

        executions.forEach { execution ->
            val executionView = LayoutInflater.from(context).inflate(R.layout.item_execution, null)
            bindExecutionView(executionView, execution)
            holder.linearLayoutExecutions.addView(executionView)
        }
    }

    private fun bindExecutionView(executionView: View, execution: Execution) {
        val textViewRepetitions = executionView.findViewById<TextView>(R.id.textViewRepetitions)
        val textViewWeight = executionView.findViewById<TextView>(R.id.textViewWeight)
        val imageViewMinus = executionView.findViewById<ImageView>(R.id.imageViewMinus)
        val imageViewEdit = executionView.findViewById<ImageView>(R.id.imageViewEdit)
        if(execution.isCardio) textViewRepetitions.text = "${execution.repetitions} minutos"
        else {
            textViewWeight.text = "${execution.kg} kg x "
            textViewRepetitions.text = "${execution.repetitions}  reps"
        }


        imageViewMinus.visibility = if (isEditing) View.VISIBLE else View.GONE
        imageViewEdit.visibility = if (isEditing) View.VISIBLE else View.GONE

        imageViewEdit.setOnClickListener { showEditDialog(execution) }
        imageViewMinus.setOnClickListener { deleteExecution(execution) }
    }

    private fun showEditDialog(execution: Execution) {
        val dialog = RegisterExecutionDialog(execution.exercise_id, execution.id, context)
        if (context is FragmentActivity) {
            dialog.show(context.supportFragmentManager, "RegistrarExercicioDialog")
            dialog.onDismissListener = {
                refreshData(execution.exercise_id)
            }
        }
    }

    private fun deleteExecution(execution: Execution) {
        runBlocking {
            executionService.deleteById(execution.id)
            refreshData(execution.exercise_id)
        }
    }

    override fun getItemCount(): Int = executionMap.size

    class ExecutionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val date: TextView = itemView.findViewById(R.id.date)
        val observationImage: ImageView = itemView.findViewById(R.id.observation)
        val linearLayoutExecutions: LinearLayout =
            itemView.findViewById(R.id.linearLayoutExecutions)
    }

    fun setOnClickObservation(imageView: ImageView, observationText: String) {
        imageView.setOnClickListener {
            Components.showPopUp(observationText, context)
        }
    }

    fun refreshData(exerciseId: String) {
        executionMap = executionService.executionListToMap(exerciseId)
        notifyDataSetChanged()
    }

    fun setEditing(isEditing: Boolean) {
        this.isEditing = isEditing
        notifyDataSetChanged()
    }
}