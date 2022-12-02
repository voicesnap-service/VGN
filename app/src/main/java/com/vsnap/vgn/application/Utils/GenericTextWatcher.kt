package com.vsnap.vgn.application.Utils

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import com.vsnap.vgn.application.R


class GenericTextWatcher(view: View, private val editText: Array<EditText>) :
    TextWatcher {
    private val view: View
    override fun afterTextChanged(editable: Editable) {
        val text = editable.toString()
        when (view.id) {
            R.id.txtOne -> if (text.length == 1) editText[1].requestFocus()
            R.id.txtTwo -> if (text.length == 1) editText[2].requestFocus() else if (text.length == 0) editText[0].requestFocus()
            R.id.txtThree -> if (text.length == 1) editText[3].requestFocus() else if (text.length == 0) editText[1].requestFocus()
            R.id.txtFour -> if (text.length == 1) editText[4].requestFocus() else if (text.length == 0) editText[2].requestFocus()
            R.id.txtFive -> if (text.length == 1) editText[5].requestFocus() else if (text.length == 0) editText[3].requestFocus()
            R.id.txtSix -> if (text.length == 0) editText[5].requestFocus()
        }
    }

    override fun beforeTextChanged(arg0: CharSequence, arg1: Int, arg2: Int, arg3: Int) {}
    override fun onTextChanged(arg0: CharSequence, arg1: Int, arg2: Int, arg3: Int) {}

    init {
        this.view = view
    }
}