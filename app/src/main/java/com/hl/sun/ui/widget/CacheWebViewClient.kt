package com.hl.sun.ui.widget

import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import android.webkit.WebViewClient
import com.hl.sun.util.Utils
import java.io.InputStream

/**
 * Function:WebViewClient 提供了一个 shouldInterceptRequest 方法用于支持外部去拦截请求，
 * WebView 每次在请求网络资源时都会回调该方法，方法入参就包含了 Url，Header 等请求参数，
 * 返回值 WebResourceResponse 即代表获取到的资源对象，默认是返回 null，即由浏览器内核自己去完成网络请求。
 * Date:2022/9/13
 * Author: sunHL
 */
class CacheWebViewClient : WebViewClient() {
    // 复写shouldInterceptRequest
    // API21以下用shouldInterceptRequest(WebView view, String url)
    override fun shouldInterceptRequest(view: WebView?, url: String?): WebResourceResponse? {
        val response = checkUrlResourceMatch(url)
        return response ?: return super.shouldInterceptRequest(view, url)
    }

    // API21以上用shouldInterceptRequest(WebView view, WebResourceRequest request)
    override fun shouldInterceptRequest(
        view: WebView?,
        request: WebResourceRequest?
    ): WebResourceResponse? {
        val response = checkUrlResourceMatch(request?.url?.toString())
        return response ?: super.shouldInterceptRequest(view, request)
    }

    private fun checkUrlResourceMatch(url: String?): WebResourceResponse? {
        // 步骤1:判断拦截资源的条件，即判断url里的图片资源的文件名
        // 此处网页里图片的url为:http://s.ip-cdn.com/img/logo.gif
        // 图片的资源文件名为:logo.gif
        if (url?.contains("13073644.png") == true) {
            var ins: InputStream? = null
            try {
                // 步骤2:创建一个输入流
                ins = Utils.getApp()?.assets?.open("webimages/replace.png")

                // 步骤3:打开需要替换的资源(存放在assets文件夹里)
                // 在app/src/main下创建一个assets文件夹
                // assets文件夹里再创建一个images文件夹,放一个error.png的图片

                // 步骤4:替换资源
                // 参数1:http请求里该图片的Content-Type,此处图片为image/png
                //WebResourceResponse response = new WebResourceResponse(
                //MimeTypeMap.getSingleton().getMimeTypeFromExtension(MimeTypeMap.getFileExtensionFromUrl(url))
                //, "UTF-8", stream);
                // 参数2:编码类型
                // 参数3:替换资源的输入流
                val response = WebResourceResponse("image/jpeg", "utf-8", ins)

                return response

            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
//                ins?.close()
            }
        }
        return null
    }
}