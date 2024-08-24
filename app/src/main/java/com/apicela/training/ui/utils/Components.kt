package com.apicela.training.ui.utils

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.widget.EditText
import android.widget.TextView
import com.apicela.training.R
import java.time.LocalDate
import java.util.Calendar
import java.util.Date
import java.util.TimeZone

class Components {
    companion object {

        fun showDatePicker(dateTimeText: EditText, context: Context) {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            // Create a DatePickerDialog with a custom onDateSet listener
            val datePickerDialog = DatePickerDialog(
                context,
                R.style.DatePickerDialogStyle, // Estilo personalizado
                { view, selectedYear, selectedMonth, selectedDay ->
                    // Logic to handle the selected date
                    val formattedMonth = concatZero(selectedMonth + 1)
                    val formattedDay = concatZero(selectedDay)
                    val selectedDateTime = "$formattedDay/$formattedMonth/$selectedYear"
                    dateTimeText.setText(selectedDateTime)
                },
                year,
                month,
                day
            )
            // Show the DatePickerDialog
            datePickerDialog.show()
        }

        fun concatZero(number: Int): String {
            return if (number < 10) {
                "0${number}" // Adiciona um zero se o mês for menor que 10
            } else {
                "${number}"
            }
        }

        fun formatDateAsString(date: LocalDate): String {
            val day = date.dayOfMonth
            val month = date.month
            val year = date.year
            return "${day}/${month}/${year}"
        }

        fun formatDateWithCurrentTime(date: Date): Date {
            val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))

            val currentHour = calendar.get(Calendar.HOUR_OF_DAY)
            val currentMinute = calendar.get(Calendar.MINUTE)
            val currentSecond = calendar.get(Calendar.SECOND)

            calendar.time = date
            calendar.set(Calendar.HOUR_OF_DAY, currentHour)
            calendar.set(Calendar.MINUTE, currentMinute)
            calendar.set(Calendar.SECOND, currentSecond)

            return calendar.time
        }


        fun showPopUp(text: String, context: Context) {
            val dialog = Dialog(context)
            dialog.setContentView(R.layout.pop_up_text)
            dialog.setCancelable(true) // Permite que o pop-up desapareça ao clicar fora

            val tvMessage = dialog.findViewById<TextView>(R.id.tvMessage)
            tvMessage.text = text
            dialog.show()
        }
    }
}