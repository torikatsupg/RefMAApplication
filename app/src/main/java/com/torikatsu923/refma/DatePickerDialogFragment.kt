package com.torikatsu923.refma


import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import java.util.*

class DatePickerDialogFragment(val isTvDeadline: Boolean) : DatePickerDialog.OnDateSetListener, DialogFragment() {
    interface OnDateSelectedListener {
        fun onSelected(year: Int, month: Int, date: Int, isTvDeadline: Boolean)
    }

    private var listener: OnDateSelectedListener? = null

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        when(context) {
            is OnDateSelectedListener -> listener = context
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val date = c.get(Calendar.DAY_OF_MONTH)
        return DatePickerDialog(requireActivity(), this,  year, month, date)
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        listener?.onSelected(year, month, dayOfMonth, isTvDeadline)
    }
}