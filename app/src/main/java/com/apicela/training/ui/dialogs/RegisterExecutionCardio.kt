package com.apicela.training.ui.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import com.apicela.training.R
import com.apicela.training.models.Execution
import com.apicela.training.services.ExecutionService
import com.apicela.training.ui.utils.Components
import kotlinx.coroutines.runBlocking
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Date

class RegisterExecutionCardio (
        private val exerciseId: String,
        private val executionId: String?,
        private val context: Context
    ) : DialogFragment() {
        val executionService: ExecutionService = ExecutionService()
        lateinit var editTextMinutes: EditText
        lateinit var editTextDate: EditText

        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
            val builder = AlertDialog.Builder(requireActivity())
            val inflater = requireActivity().layoutInflater
            val view = inflater.inflate(R.layout.register_exercise_cardio, null)
            builder.setView(view)
            editTextMinutes = view.findViewById<EditText>(R.id.editTextMinutes)
            editTextDate = view.findViewById<EditText>(R.id.editTextDate)
            val buttonConfirmar = view.findViewById<Button>(R.id.buttonConfirmar)
            val buttonCancelar = view.findViewById<Button>(R.id.buttonCancelar)

            setOnClick()
            if (executionId == null) {
                val lastExercise = runBlocking { executionService.getLastInsertedExecution(exerciseId) }
                if (lastExercise !== null) {
                    editTextMinutes.setText("${lastExercise.kg}")
                }
            } else {
                val executionToUpdate = runBlocking { executionService.getExecutionById(executionId) }
                if (executionToUpdate !== null) {
                    editTextMinutes.setText("${executionToUpdate.kg}")
                }
            }


            var date = Instant.now().atZone(ZoneId.systemDefault()).toLocalDateTime().format(
                DateTimeFormatter.ofPattern("dd/MM/yyyy")
            )
            editTextDate.setText(date as String)
            editTextDate.setOnClickListener {
                Components.showDatePicker(editTextDate, context)
                editTextDate.requestFocus() // Request focus after showing the DatePicker
            }
            buttonConfirmar.setOnClickListener {
                // Aqui você pode obter os valores dos EditTexts e fazer o que quiser com eles
                val minutes = editTextMinutes.text.toString().toIntOrNull() ?: 0
                val format = SimpleDateFormat("dd/MM/yyyy")
                val editTextAsDate = format.parse(editTextDate.text.toString()) as Date
                val date = Components.formatDateWithCurrentTime(editTextAsDate)
                if (executionId == null) {
                    val execution = Execution(minutes, true,0f, exerciseId, date)
                    runBlocking { executionService.addExecutionToDatabase(execution) }
                } else {
                    val execution = Execution(executionId, true, minutes, 0.0f, exerciseId, date)
                    runBlocking {
                        executionService.updateExecutionObject(execution)
                    }
                }
                dismiss() // Fecha o diálogo após a confirmação
            }

            buttonCancelar.setOnClickListener {
                dismiss() // Fecha o diálogo sem fazer nada
            }

            return builder.create()
        }

        private fun setOnClick() {
            setOnClickClearInputField(editTextMinutes)
        }

        var onDismissListener: (() -> Unit)? = null

        fun setOnClickClearInputField(field: Any) {
            if (field is EditText) {
                field.setOnClickListener {
                    field.setText("")
                }
                // focusable
//            field.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
//                if (hasFocus) {
//                    field.setText("")
//                }
//            }
            }
        }

        override fun onDismiss(dialog: DialogInterface) {
            super.onDismiss(dialog)
            onDismissListener?.invoke()
        }
    }