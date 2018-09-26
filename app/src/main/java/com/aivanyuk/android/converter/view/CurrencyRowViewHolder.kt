package com.aivanyuk.android.converter.view

import android.annotation.SuppressLint
import android.support.v7.widget.RecyclerView
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import com.aivanyuk.android.converter.ImageLoader
import com.aivanyuk.android.converter.presenter.CurrencyPresenter
import kotlin.math.abs


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

    var startX: Float = 0f
    var startY: Float = 0f
    var endX: Float = 0f
    var endY: Float = 0f

    init {

        viewImpl.input.setOnTouchListener { _: View, event: MotionEvent ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    startX = event.x

                    startY = event.y
                    return@setOnTouchListener false
                }
                MotionEvent.ACTION_UP -> {
                    endX = event.x
                    endY = event.y
                    if (isClick()) {
                        presenter.requestPivot(viewImpl.pos)
                        return@setOnTouchListener true
                    } else {
                        return@setOnTouchListener false
                    }
                }
                else -> return@setOnTouchListener false
            }
        }

    }

    private val touchSlop = ViewConfiguration.get(itemView.context).scaledTouchSlop
    private fun isClick(): Boolean {
        val diffX = Math.abs(startX - endX)
        val diffY = abs(endY - startY)
        return !(diffX > touchSlop || diffY > touchSlop)
    }

}
