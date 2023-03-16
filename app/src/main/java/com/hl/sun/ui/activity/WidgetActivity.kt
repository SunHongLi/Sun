package com.hl.sun.ui.activity

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.hl.sun.R
import com.hl.sun.bean.SignSentence
import com.hl.sun.ui.widget.AutoLinkStyleTextView
import com.hl.sun.ui.widget.TextLineInfo
import com.hl.sun.ui.widget.UnderLineNoteTextView
import com.hl.sun.util.Utils
import com.hl.weblib.core.util.GsonUtils

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

        setAutoLinkStyleTextView()

        setUnderLineNoteTextView()
    }

    private fun setUnderLineNoteTextView() {
        val result =
            GsonUtils.parseFromAssetJson(Utils.getApp(), "json0.json", SignSentence::class.java)
                ?: return

        val lineNoteTextView = findViewById<UnderLineNoteTextView>(R.id.note_tv_underline)
        var infos = getSentenceInfo(result)
        lineNoteTextView.text = contentString.toString()
        lineNoteTextView.setUnderInfo(infos)
    }

    var currentLength = 0
    var contentString: StringBuilder = StringBuilder()
    private fun getSentenceInfo(result: SignSentence): ArrayList<TextLineInfo> {
        currentLength = 0
        contentString.clear()
        val infos = arrayListOf<TextLineInfo>()
        getSentence(infos, result)
        return infos
    }

    private fun getSentence(infos: MutableList<TextLineInfo>, result: SignSentence) {
        if (result.children?.isNotEmpty() == true) {
            result.children?.forEach {
                it?.let { getSentence(infos, it) }
            }
        } else {
            result.content?.let {
                if (result.hasScore) {
                    infos.add(
                        TextLineInfo(currentLength, currentLength + it.length, "${result.score}分")
                    )
                }
                val length = result.content?.length ?: 0

                currentLength += length
                contentString.append(it)
            }
        }
    }

    private fun setAutoLinkStyleTextView() {
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