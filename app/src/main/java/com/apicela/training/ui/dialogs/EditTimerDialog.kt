package com.apicela.training.ui.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.GridLayout
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.apicela.training.Apicela
import com.apicela.training.R

class EditTimerDialog() : DialogFragment() {
    var confirmed: Boolean = false
    lateinit var myView : View
    lateinit var editText : EditText
    lateinit var gridLayoutButtons: GridLayout

    private fun changeEditTextValue(button : Button){
        val buttonText = button.text.toString()
        editText.setText(buttonText)
    }
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireActivity())
        val inflater = requireActivity().layoutInflater
        myView = inflater.inflate(R.layout.dialog_edit_timing, null)
        editText = myView.findViewById<EditText>(R.id.newTimeText)
        editText.setText(Apicela.REST_TIMING)
        gridLayoutButtons = myView.findViewById(R.id.buttonsTimingGridLayout)
        setUpOnClick()
        builder.setView(myView)
        val buttonConfirmar = myView.findViewById<Button>(R.id.buttonConfirmar)

        buttonConfirmar.setOnClickListener {
            confirmed = true
            Apicela.getInstance().updateClockTime(editText.text.toString())
            Toast.makeText(context, "Tempo de descanso atualizado!", Toast.LENGTH_SHORT).show()
            dismiss() // Fecha o diálogo após a confirmação
        }
        editText.addTextChangedListener(object : TextWatcher {
            private var isUpdating = false
            private val timeFormat = "##:##"

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (isUpdating) {
                    isUpdating = false
                    return
                }

                var text = s.toString().filter { it.isDigit() }

                if (text.length > 4) {
                    text = text.substring(0, 4) // Limita a 4 dígitos
                }

                if (text.length == 4) {
                    text = "${text.substring(0, 2)}:${text.substring(2)}"
                }

                isUpdating = true
                editText.setText(text)
                editText.setSelection(text.length)
            }

            override fun afterTextChanged(s: Editable?) {}
        })
        return builder.create()
    }

    private fun setUpOnClick() {
        // grid layout buttons
        for(i in 0 until gridLayoutButtons.childCount){
            val child = gridLayoutButtons.getChildAt(i);
            if(child is Button) child.setOnClickListener { changeEditTextValue(it as Button) }
        }
    }

    var onDismissListener: (() -> Unit)? = null

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        onDismissListener?.invoke()
    }
}