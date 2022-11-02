package com.hl.sun.ui.activity

import android.os.Bundle
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.hl.sun.R
import com.hl.sun.java.JavaCode
import com.hl.sun.ui.widget.CacheWebViewClient
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
        webview.webViewClient=CacheWebViewClient();
        webview.loadUrl("https://www.pexels.com/zh-cn/")

        //本地zip包（含.js文件）
//        webview.webViewClient = OfflineWebViewClient()
//        webview.loadUrl("http://122.51.132.117/")
//        JavaCode.method(webview)
//        webview.loadUrl("file:///android_asset/mobile-web-best-practice.html");
//        webview.loadUrl("mini://main/mobile-web-best-practice.html")


//        webview.webViewClient= WebViewClient()
//        webview.loadUrl("file:///android_asset/test/test.html");

        /*
        从本地加载test.html会有跨域问题,要调用如下方法
        public static void method(WebView mwebview) {
        try {//本地HTML里面有跨域的请求 原生webview需要设置之后才能实现跨域请求
            if (Build.VERSION.SDK_INT >= 16) {
                Class<?> clazz = mwebview.getSettings().getClass();
                Method method = clazz.getMethod(
                        "setAllowUniversalAccessFromFileURLs", boolean.class);
                if (method != null) {
                    method.invoke(mwebview.getSettings(), true);
                }
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

    }
         */
    }


}