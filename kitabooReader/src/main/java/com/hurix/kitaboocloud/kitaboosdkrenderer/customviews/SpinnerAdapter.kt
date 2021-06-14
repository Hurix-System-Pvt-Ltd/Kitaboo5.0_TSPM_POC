package com.hurix.kitaboocloud.kitaboosdkrenderer.customviews

import android.content.Context
import android.graphics.Typeface
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

import com.hurix.customui.views.ScalableEditText

class SpinnerAdapter public constructor(context: Context, resource: Int, private val font: Typeface, items: Array<String>) : ArrayAdapter<String>(context, resource, items) {

    // Affects default (closed) state of the spinner
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = super.getView(position, convertView, parent) as TextView
        view.typeface = font
        return view
    }

    // Affects opened state of the spinner
    /*override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = super.getDropDownView(position, convertView, parent) as TextView
        view.setText("7")
        view.typeface = font
        return view
    }*/
}
