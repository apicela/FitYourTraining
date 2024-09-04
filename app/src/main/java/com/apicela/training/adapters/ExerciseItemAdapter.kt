package com.apicela.training.adapters

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.apicela.training.ItemTouchHelperAdapter
import com.apicela.training.R
import com.apicela.training.interfaces.ExerciseAdapterInterface
import com.apicela.training.interfaces.OnExerciseCheckedChangeListener
import com.apicela.training.models.Exercise
import com.apicela.training.services.ExerciseService
import com.apicela.training.ui.activitys.CreateExercise
import com.apicela.training.ui.activitys.ExecutionActivity
import com.apicela.training.ui.activitys.ExerciseActivity
import com.apicela.training.ui.utils.ImageHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Collections

class ExerciseItemAdapter(
    private val context: Context,
    private val divisionId: String? = null,
    private val exercises: List<Exercise>? = null,
    private val checkedItems: MutableList<Exercise>? = null,
    private val checkedItemCountChangedListener: OnExerciseCheckedChangeListener? = null
) :
    RecyclerView.Adapter<ExerciseItemAdapter.ExerciseItemViewHolder>(), ExerciseAdapterInterface,
    ItemTouchHelperAdapter {

    private var isEditing = false
    private val exerciseService = ExerciseService()
    private var exerciseList: List<Exercise> = listOf()

    init {
        refreshData()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseItemViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_exercise, parent, false)
        return ExerciseItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ExerciseItemViewHolder, position: Int) {
        val exercise = exerciseList[position]
        holder.bind(exercise)
    }

    inner class ExerciseItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val exerciseName: TextView = itemView.findViewById(R.id.exercise_text)
        private val exerciseImage: ImageView = itemView.findViewById(R.id.exercise_image)
        private val checkbox: CheckBox = itemView.findViewById(R.id.checkbox)
        private val minusImage: ImageView = itemView.findViewById(R.id.minus)
        private val editButton: ImageView = itemView.findViewById(R.id.edit)

        fun bind(exercise: Exercise) {
            exerciseName.text = exercise.name
            ImageHelper.setImage(context, exerciseImage, exercise.image, true)
            setVisibility()
            setOnCheckedChangeListener(exercise)
            setOnClickListeners(exercise)
        }


        private fun setVisibility() {
            checkbox.visibility =
                if (checkedItemCountChangedListener != null) View.VISIBLE else View.GONE
            minusImage.visibility = if (isEditing) View.VISIBLE else View.GONE
            editButton.visibility = if (isEditing) View.VISIBLE else View.GONE
        }

        private fun setOnCheckedChangeListener(exercise: Exercise) {
            checkbox.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    checkedItems?.add(exercise)
                } else {
                    checkedItems?.remove(exercise)
                }
                checkedItemCountChangedListener?.onCheckedItemCountChanged(checkedItems?.size ?: 0)
            }
        }

        private fun setOnClickListeners(exercise: Exercise) {
            minusImage.setOnClickListener {
                CoroutineScope(Dispatchers.IO).launch {
                    if (divisionId != null) {
                        exerciseService.removeExerciseFromDivision(divisionId, exercise.id)
                        exerciseList = exerciseService.getExerciseListFromDivision(divisionId)
                    } else {
                        exerciseService.deleteExerciseById(exercise.id)
                    }
                    withContext(Dispatchers.Main) {
                        if (context is ExerciseActivity) context.refreshExerciseAdapter()
                    }
                }
            }

            editButton.setOnClickListener {
                val intent = Intent(context, CreateExercise::class.java)
                intent.putExtra("exerciseId", exercise.id)
                context.startActivity(intent)
            }

            if (!isEditing && checkedItemCountChangedListener == null) {
                itemView.setOnClickListener {
                    val intent = Intent(context, ExecutionActivity::class.java)
                    Log.d("ExerciseItemAdapter", exercise.toString())
                    intent.putExtra("exercise_id", exercise.id)
                    exercise.metricType?.let { metricType ->
                        intent.putExtra("metric", metricType.toString())
                    }
                    intent.putExtra("exercise_image", exercise.image)
                    intent.putExtra("exercise_name", exercise.name)
                    context.startActivity(intent)
                }
            }
        }
    }

    override fun getItemCount(): Int = exerciseList.size

    override fun refreshData() {
        CoroutineScope(Dispatchers.IO).launch {
            exerciseList = getExerciseList()
            withContext(Dispatchers.Main) {
                notifyDataSetChanged()
            }
        }
    }

    private suspend fun getExerciseList(): List<Exercise> {
        return exercises ?: exerciseService.getExerciseListFromDivision(divisionId)
    }

    override fun setEditing(isEditing: Boolean) {
        this.isEditing = isEditing
        notifyDataSetChanged()
    }

    override fun onItemMove(fromPosition: Int, toPosition: Int): Boolean {
        Collections.swap(exerciseList, fromPosition, toPosition)
        CoroutineScope(Dispatchers.IO).launch {
            exerciseService.notifyListExercisesFromDivisionChanged(divisionId!!, exerciseList.map {  it.id})
        }
        notifyItemMoved(fromPosition, toPosition)
        return true
    }
}
