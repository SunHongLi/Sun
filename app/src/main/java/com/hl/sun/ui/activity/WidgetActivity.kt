package com.hl.sun.ui.activity

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.hl.sun.R
import com.hl.sun.ui.widget.AutoLinkStyleTextView
import com.hl.sun.ui.widget.TextLineInfo
import com.hl.sun.ui.widget.UnderLineNoteTextView

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


        val infos = arrayListOf(
            TextLineInfo(0, 3, "0.3分"), TextLineInfo(9, 11, "0.5分"),
            TextLineInfo(44, 46, "1.0分"), TextLineInfo(49, 50, "0.1分")
        )
        findViewById<UnderLineNoteTextView>(R.id.note_tv_underline).setUnderInfo(infos)
    }

}