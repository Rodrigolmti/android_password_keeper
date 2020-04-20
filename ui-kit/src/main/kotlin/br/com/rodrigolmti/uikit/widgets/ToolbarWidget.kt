package br.com.rodrigolmti.uikit.widgets

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import br.com.rodrigolmti.ui_kit.R
import kotlinx.android.synthetic.main.toolbar.view.*

class ToolbarWidget : FrameLayout {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        applyStyle()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        applyStyle()
    }

    private fun applyStyle() {
        inflate(context, R.layout.toolbar, this)
        imgBack.setOnClickListener {
            onBackClick?.invoke()
        }
    }

    var title: String? = null
        get() = tvTitle.text.toString()
        set(text) {
            tvTitle.text = text
            field = text
        }

    var onBackClick: (() -> Unit)? = null
}