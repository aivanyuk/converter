package com.aivanyuk.android.converter.view

import android.annotation.SuppressLint
import android.support.v7.widget.RecyclerView
import android.view.MotionEvent
import android.view.View
import com.aivanyuk.android.converter.ImageLoader
import com.aivanyuk.android.converter.presenter.CurrencyPresenter


@SuppressLint("ClickableViewAccessibility")
class CurrencyRowViewHolder(private val presenter: CurrencyPresenter,
                            imageLoader: ImageLoader,
                            itemView: View) : RecyclerView.ViewHolder(itemView), CurrencyItemView {
    override fun setImage(url: String) {
        viewImpl.setImage(url)
    }

    override fun setName(text: CharSequence) {
        viewImpl.setName(text)
    }

    override fun setDescription(text: CharSequence) {
        viewImpl.setDescription(text)
    }

    override fun setAmount(text: CharSequence) {
        viewImpl.setAmount(text)
    }

    override fun setPosition(position: Int) {
        viewImpl.pos = position
    }

    private val viewImpl = CurrencyItemViewImpl(itemView, imageLoader)


    init {
        viewImpl.input.setOnTouchListener { _: View, event: MotionEvent ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    presenter.requestPivot(viewImpl.pos)
                    return@setOnTouchListener true
                }
                else -> return@setOnTouchListener false
            }
        }

    }


}
