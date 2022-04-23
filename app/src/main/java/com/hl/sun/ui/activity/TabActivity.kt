package com.hl.sun.ui.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.hl.sun.R
import com.hl.sun.ui.widget.BottomTabView
import com.hl.sun.ui.widget.BottomTabView.OnTabSelectedListener


class TabActivity : AppCompatActivity() {
    var count: Int = 100
    val bottomTabView by lazy { findViewById<BottomTabView>(R.id.bottom_tab) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tab)

        bottomTabView.onTabSelectedListener = object : OnTabSelectedListener {
            override fun onTabSelected(index: Int) {
                println("tab:$index")
                Toast.makeText(this@TabActivity, index.toString(), Toast.LENGTH_SHORT).show()
            }

            override fun onTabReselect(index: Int) {
                Toast.makeText(this@TabActivity, "reselect:$index", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun setBadge(view: View) {
        bottomTabView.setReciteWordBadge(count.toString())
        count--
    }

    fun cancelBadge(view: View) {
        bottomTabView.hideReciteWordBadge()
//        val intent =
//            Intent(Intent.ACTION_VIEW, Uri.parse("gaodunrwapp://rw/set?group_id=123"))
////            Intent(Intent.ACTION_VIEW, Uri.parse("gaodunapp://gd/landscape/web?src=https://www.json.cn/"))
//        startActivity(intent)
    }
}