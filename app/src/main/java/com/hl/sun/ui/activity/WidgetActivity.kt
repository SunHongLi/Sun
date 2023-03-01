package com.hl.sun.ui.activity

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.hl.sun.R
import com.hl.sun.ui.widget.AutoLinkStyleTextView

/**
 * Function:自定义控件展示
 * Date:2023/3/1
 * Author: sunHL
 */
class WidgetActivity : AppCompatActivity() {
    private var autoLinkStyleTextView: AutoLinkStyleTextView? = null
    private var tvStartImage: AutoLinkStyleTextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_widget)


        autoLinkStyleTextView = findViewById(R.id.tv)
        autoLinkStyleTextView?.setOnClickCallBack { position ->
            if (position == 0) {
                Toast.makeText(this@WidgetActivity, "购买须知", Toast.LENGTH_SHORT).show()
            } else if (position == 1) {
                Toast.makeText(this@WidgetActivity, "用户条款", Toast.LENGTH_SHORT).show()
            }
        }
        tvStartImage = findViewById(R.id.tv_start_image)
        tvStartImage?.setStartImageText(tvStartImage?.text)
    }

}