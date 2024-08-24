package com.apicela.training.utils

import android.view.View
import android.widget.CheckBox
import android.widget.Spinner

class UtilsComponents {
    companion object {
        fun getSpinnerSelectedItem(spinner: Spinner): String {
            val selectedIndex = spinner.selectedItemPosition
            val spinnerItem = spinner.getItemAtPosition(selectedIndex).toString()
            return spinnerItem
        }

        fun <T> turnListOfViewVisible(listOfView: List<T>, visible: Boolean) {
            if (listOfView.all { it is CheckBox }) {
                if (visible) {
                    listOfView.forEach { (it as CheckBox).visibility = View.VISIBLE }
                } else {
                    listOfView.forEach { (it as CheckBox).visibility = View.INVISIBLE }
                }
            }
        }

//        fun countCheckBox(listOfCheckBox : List<CheckBox>) : Int{
//            var checkedCount = 0
//                for (checkBox in listOfCheckBox) {
//                    if (checkBox.isChecked) {
//                        checkedCount++
//                    }
//                }
//            return checkedCount
//        }


    }
}