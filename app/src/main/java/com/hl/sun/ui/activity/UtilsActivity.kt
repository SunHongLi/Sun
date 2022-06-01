package com.hl.sun.ui.activity

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.hl.sun.R
import java.text.DecimalFormat

class UtilsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_utils)
    }

    fun testDecimalForamt(view: View) {
        println("DecimalFormat:" + DecimalFormat("###,###").format(12345677810))//12,345,677,810
        println("DecimalFormat:" + DecimalFormat("###,###").format(12345677810.13))//12,345,677,810
        println("DecimalFormat:" + DecimalFormat("###,###").format(0))//0
        println("DecimalFormat:" + DecimalFormat("###,###").format(0.00))//0
        println("DecimalFormat:" + DecimalFormat("###,###").format(810))//810
        println("DecimalFormat:" + DecimalFormat("###,###.00").format(23))//23.00
        println("DecimalFormat:" + DecimalFormat("###,###").format(0.000))//0
    }
}