package com.hl.sun.ui.activity

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.hl.sun.R
import com.hl.sun.bean.GroupByBean
import java.text.DecimalFormat

class UtilsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_utils)
    }

    fun testDecimalForamt(view: View) {
        //注意：format()传String会报错
        println("DecimalFormat:" + DecimalFormat("###,###").format(12345677810))//12,345,677,810
        println("DecimalFormat:" + DecimalFormat("###,###").format(12345677810.13))//12,345,677,810
        println("DecimalFormat:" + DecimalFormat("###,###").format(0))//0
        println("DecimalFormat:" + DecimalFormat("###,###").format(0.00))//0
        println("DecimalFormat:" + DecimalFormat("###,###").format(810))//810
        println("DecimalFormat:" + DecimalFormat("###,###.00").format(23))//23.00
        println("DecimalFormat:" + DecimalFormat("###,###").format(0.000))//0
    }

    fun testGroupBy(view: View) {
        var list = listOf<GroupByBean>(
            GroupByBean("00", "title-0"),
            GroupByBean("01", "title-01-1"),
            GroupByBean("01", "title-01-2"),
            GroupByBean("22", "title-22-1"),
            GroupByBean("22", "title-22-2"),
            GroupByBean("22", "title-22-3"),
        )

        // Map<String?, List<GroupByBean>>
        var result = list.groupBy {
            it.id
        }
        println("group by test:$result")
        //{00=[GroupByBean(title=title-0)],
        // 01=[GroupByBean(title=title-01-1), GroupByBean(title=title-01-2)],
        // 22=[GroupByBean(title=title-22-1), GroupByBean(title=title-22-2), GroupByBean(title=title-22-3)]}

        /**-----------------------------------------------------------------------------**/

        //List<Pair<String?, List<GroupByBean>>>
        var resultList = list.groupBy {
            it.id
        }.toList()
        println("group by & toList test:$resultList")
        //[(00, [GroupByBean(title=title-0)]),
        // (01, [GroupByBean(title=title-01-1), GroupByBean(title=title-01-2)]),
        // (22, [GroupByBean(title=title-22-1), GroupByBean(title=title-22-2), GroupByBean(title=title-22-3)])]

        /**-----------------------------------------------------------------------------**/

        var newList = arrayListOf<List<GroupByBean>>()
        list.groupBy {
            it.id
        }.toList().forEach { newList.add(it.second) }
        println("group by second test:$newList")
        //[
        // [GroupByBean(title=title-0)],
        // [GroupByBean(title=title-01-1), GroupByBean(title=title-01-2)],
        // [GroupByBean(title=title-22-1), GroupByBean(title=title-22-2), GroupByBean(title=title-22-3)]
        // ]
    }
}