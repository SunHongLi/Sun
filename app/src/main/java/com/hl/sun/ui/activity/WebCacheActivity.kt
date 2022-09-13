package com.hl.sun.ui.activity

import android.os.Bundle
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.hl.sun.R
import com.hl.sun.ui.widget.CacheWebViewClient
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

        // 加载需要显示的网页
//        webview.loadUrl("http://ip.cn/")
        webview.loadUrl("https://www.pexels.com/zh-cn/")

//        webview.webViewClient= WebViewClient()
        webview.webViewClient = CacheWebViewClient()
    }

}