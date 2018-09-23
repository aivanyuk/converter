package com.aivanyuk.android.converter.view

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.aivanyuk.android.converter.R

class CurrencyRowViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val icon = itemView.findViewById<ImageView>(R.id.icon)
    val name = itemView.findViewById<TextView>(R.id.text_name)
    val description = itemView.findViewById<TextView>(R.id.text_description)
    val input = itemView.findViewById<EditText>(R.id.input_amount)

    init {
        name.text = "RUB"
        description.text = "Russian Ruble"
        input.setText("800")
    }
}
