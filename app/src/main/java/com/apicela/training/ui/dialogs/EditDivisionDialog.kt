package com.apicela.training.ui.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import com.apicela.training.R

class EditDivisionDialog(var divisionName: String, var image: String) : DialogFragment() {
    var confirmed: Boolean = false

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireActivity())
        val inflater = requireActivity().layoutInflater
        val view = inflater.inflate(R.layout.edit_division, null)
        builder.setView(view)
        val divisionNameView = view.findViewById<EditText>(R.id.divisionNameText)
        val imageView = view.findViewById<EditText>(R.id.imageUrlText)
        val buttonConfirmar = view.findViewById<Button>(R.id.buttonConfirmar)
        val buttonCancelar = view.findViewById<Button>(R.id.buttonCancelar)

        divisionNameView.setText(divisionName)
        imageView.setText(image)

        buttonConfirmar.setOnClickListener {
            confirmed = true
            divisionName = divisionNameView.text.toString()
            image = imageView.text.toString()
            dismiss()
        }

        buttonCancelar.setOnClickListener {
            confirmed = false;
            dismiss() // Fecha o diálogo sem fazer nada
        }

        return builder.create()
    }

    var onDismissListener: (() -> Unit)? = null

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        onDismissListener?.invoke()
    }
}