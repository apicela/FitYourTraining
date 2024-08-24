package com.apicela.training.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.apicela.training.R
import com.apicela.training.interfaces.ExerciseAdapterInterface
import com.apicela.training.interfaces.OnExerciseCheckedChangeListener
import com.apicela.training.models.Exercise
import com.apicela.training.models.extra.Muscle
import com.apicela.training.services.ExerciseService
import com.apicela.training.ui.utils.ImageHelper
import com.google.android.material.imageview.ShapeableImageView
import kotlinx.coroutines.runBlocking


class ExerciseAdapter(
    private val context: Context,
    private var exerciseMap: Map<String, List<Exercise>>,
    private val divisionId: String? = null,
    private val exerciseService: ExerciseService,
    private val checkedItemCountChangedListener: OnExerciseCheckedChangeListener? = null // Adicionando a interface
) :
    RecyclerView.Adapter<ExerciseAdapter.ExerciseViewHolder>(), ExerciseAdapterInterface {
    private var checkedItems = mutableListOf<Exercise>();
    private var isEditing = false;
    private val originalExerciseMap : Map<String, List<Exercise>> = exerciseMap
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseViewHolder {
        val view =
            LayoutInflater.from(context).inflate(R.layout.cardview_exercise_layout, parent, false)
        return ExerciseViewHolder(view)
    }

    override fun onBindViewHolder(holder: ExerciseViewHolder, position: Int) {
        val key = exerciseMap.keys.elementAt(position)
        setUpViews(holder, key)

        val exercises = exerciseMap.getValue(key)
        setUpRecyclerView(holder, exercises)
        (holder.recyclerView.adapter as? ExerciseItemAdapter)?.setEditing(isEditing)

    }

    private fun setUpRecyclerView(holder: ExerciseViewHolder, exercises: List<Exercise>) {
        holder.recyclerView.layoutManager = LinearLayoutManager(context)
        holder.recyclerView.adapter = ExerciseItemAdapter(
            context,
            divisionId,
            exercises,
            checkedItems,
            checkedItemCountChangedListener
        )
    }

    private fun setUpViews(holder: ExerciseAdapter.ExerciseViewHolder, key: String) {
        holder.muscle_name.text = Muscle.getMuscleByPtbr(key).uppercase()
        ImageHelper.setImage(
            context,
            holder.muscle_image,
            "muscle_group_${(key).lowercase()}",
            false
        )
    }

    override fun getItemCount(): Int = exerciseMap.size

    class ExerciseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val muscle_name: TextView = itemView.findViewById(R.id.muscle_name)
        val muscle_image: ShapeableImageView = itemView.findViewById(R.id.muscle_image)
        val recyclerView: RecyclerView = itemView.findViewById(R.id.recyclerViewExercises)
    }

    override fun refreshData() {
        runBlocking {
            exerciseMap = exerciseService.exerciseListToMap(divisionId)!!
            notifyDataSetChanged()
        }
    }

     fun filterList(filter: String) {
        exerciseMap = if (filter.isEmpty()) {
            originalExerciseMap
        } else {
            originalExerciseMap.mapValues { (_, exercises) ->
                exercises.filter { it.name.contains(filter, ignoreCase = true) }
            }.filterValues { it.isNotEmpty() }
        }
        notifyDataSetChanged()
    }

    fun getSelectedExercises(): List<Exercise> {
        return checkedItems;
    }

    override fun setEditing(isEditing: Boolean) {
        this.isEditing = isEditing
        notifyDataSetChanged()
    }
}
