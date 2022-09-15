package com.hl.sun.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.hl.sun.R
import com.hl.weblib.OfflineWebViewClient
import kotlinx.android.synthetic.main.activity_web_cache.*

class WebCacheActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_cache)

        setCacheLoadPath()
    }

    private fun setCacheLoadPath() {
        // 支持与JS交互
        webview.settings.javaScriptEnabled = true

        //只变更图片的，对应CacheWebViewClient()
        //webview.loadUrl("https://www.pexels.com/zh-cn/")

        //本地zip包（含.js文件）
        webview.webViewClient = OfflineWebViewClient()
        webview.loadUrl("http://122.51.132.117/")
    }

}