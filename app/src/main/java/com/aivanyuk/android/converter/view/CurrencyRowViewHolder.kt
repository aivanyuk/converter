package com.aivanyuk.android.converter.view

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.aivanyuk.android.converter.ImageLoader
import com.aivanyuk.android.converter.R
import com.aivanyuk.android.converter.presenter.CurrencyPresenter

class CurrencyRowViewHolder(private val presenter: CurrencyPresenter,
                            private val imageLoader: ImageLoader,
                            itemView: View) : RecyclerView.ViewHolder(itemView), CurrencyItemView {
    val imageSizeW: Int = itemView.resources.getDimensionPixelSize(R.dimen.image_flag_size_w)
    val imageSizeH: Int = itemView.resources.getDimensionPixelSize(R.dimen.image_flag_size_h)

    val icon = itemView.findViewById<ImageView>(R.id.icon)
    val name = itemView.findViewById<TextView>(R.id.text_name)
    val description = itemView.findViewById<TextView>(R.id.text_description)
    val input = itemView.findViewById<EditText>(R.id.input_amount)
    var pos: Int = 0
    var url: String = ""

    init {
        input.setOnClickListener { presenter.requestPivot(pos) }
    }

    override fun setImage(url: String) {
        if (this.url != url) {
            imageLoader.loadImage(url, Pair(imageSizeW, imageSizeH), icon)
            this.url = url
        }
    }

    override fun setName(text: CharSequence) {
        name.text = text
    }

    override fun setPosition(position: Int) {
        pos = position
    }

    override fun setDescription(text: CharSequence) {
        description.text = text
    }

    override fun setAmount(text: CharSequence) {
        input.setText(text)
    }


}
