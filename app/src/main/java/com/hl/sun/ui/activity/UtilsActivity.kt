package com.hl.sun.ui.activity

import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.DynamicDrawableSpan
import android.text.style.ForegroundColorSpan
import android.text.style.ImageSpan
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.hl.sun.R
import com.hl.sun.bean.EqualsBean
import com.hl.sun.bean.GroupByBean
import com.hl.sun.java.JavaCode
import com.hl.sun.util.LocationTest
import com.hl.sun.util.TimeUtils
import kotlinx.android.synthetic.main.activity_utils.*
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

    /**
     * 判断两个对象的值是否相同，通过 ==（或者equals） ，如果判断对象是否相同则需要使用 ===
     * 在kotlin中如果两边的参数都不为null,  `==`就会被翻译成equals()方法
     */
    fun testEquals(view: View) {
        ///-------------GroupByBean：没有重写equals()和hashCode()------------------------
        var bean1 = GroupByBean("1", "title1")
        var bean2 = GroupByBean("2", "title2")
        var bean3 = GroupByBean("2", "title2")
        Log.i("testEquals1", "${bean1 == bean2}") //false
        Log.i("testEquals2", "${bean1.equals(bean2)}")//false
        Log.i("testEquals3", "${bean1 === bean2}")//false

        Log.i("testEquals4", "${bean2 == (bean3)}")//false
        Log.i("testEquals5", "${bean2.equals(bean3)}")//false
        Log.i("testEquals6", "${bean2 === (bean3)}")//false

        ///-------------EqualsBean：重写了equals()和hashCode()------------------------
        var user1 = EqualsBean("1", "name1")
        var user2 = EqualsBean("2", "name2")
        var user3 = EqualsBean("2", "name2")
        Log.i("testEquals11", "${user1 == user2}") //false
        Log.i("testEquals22", "${user2 == user3}") //true (只重写equals，也是true，但是不规范)

        val map = HashMap<EqualsBean, String>()
        map[user1] = "map-value-1"
        map[user2] = "map-value-2"
        map[user3] = "map-value-3"
        Log.i("testEquals-map-0", "${map.size}") //2  (未重写hashCode()时，此处是3)
        Log.i("testEquals-map-1", "${map[user1]}") //map-value-1
        Log.i("testEquals-map-2", "${map[user2]}") //map-value-3  (未重写hashCode()时，此处是map-value-2)
        Log.i("testEquals-map-3", "${map[user3]}") //map-value-3
    }

    /**
     * 原生获取定位信息
     */
    fun getLocation(view: View) {
        LocationTest.fetchLocation()
    }

    /**
     * 另一种写法参考AutoLinkStyleTextView
     */
    fun spannableString(view: View) {
        //ImageSpan：将文本替换为图片；
        val s0 = ImageSpan(this, R.drawable.icon_good, DynamicDrawableSpan.ALIGN_CENTER)

        val s1 = "=您已击败全国"

        val s2 = SpannableString("88%")//这里不能传null，但空字符串可以
        s2.setSpan(
            ForegroundColorSpan(Color.parseColor("#FFFE05")),
            0, s2.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        val s3 = "考生，请继续加油，再接再厉"

        val sb = SpannableStringBuilder()
        sb.append(s1)
        sb.append(s2)
        sb.append(s3)
        sb.setSpan(s0, 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        tv_span.text = sb
    }

    fun simpleDateFormat(view: View) {
        val timestamp = 1675768982000

        val weekReplace = "%"
        val formatTimeStamp =
            TimeUtils.formatTimeStamp(timestamp, "yyyy.MM.dd(${weekReplace}) HH:mm")

        btn_date_format.text =
            formatTimeStamp?.replace(weekReplace, TimeUtils.getWeek(timestamp) ?: "").toString()
    }

    fun subZeroAndDot(view: View) {
        println("===================  2.00:${JavaCode.subZeroAndDot("2.00")}")
        println("===================  12.20:${JavaCode.subZeroAndDot("12.20")}")
        println("===================  2.400:${JavaCode.subZeroAndDot("2.400")}")
        println("===================  2.480:${JavaCode.subZeroAndDot("2.480")}")
        println("===================  2:${JavaCode.subZeroAndDot("2")}")
        println("===================  20:${JavaCode.subZeroAndDot("20")}")
        println("===================  200:${JavaCode.subZeroAndDot("200")}")
        println("===================  200.0:${JavaCode.subZeroAndDot("200.0")}")
        println("===================  200.0000:${JavaCode.subZeroAndDot("200.0000")}")
        println("===================  201.0000:${JavaCode.subZeroAndDot("201.0000")}")
        println("===================  201.010:${JavaCode.subZeroAndDot("201.010")}")
        println("===================  201.01:${JavaCode.subZeroAndDot("201.01")}")
    }

    /// >=1000000后4位缩写为w
    fun formatW(view: View) {
        println("formatNumber======null:${formatNumber(null)}")
        println("formatNumber======0:${formatNumber("0")}")
        println("formatNumber======'':${formatNumber("")}")
        println("formatNumber======哈:${formatNumber("哈")}")
        println("formatNumber======uu评价嘎吱fgg尴尬:${formatNumber("uu评价嘎吱fgg尴尬")}")
        println("formatNumber======123:${formatNumber("123")}")
        println("formatNumber====== 999998:${formatNumber("999998")}")
        println("formatNumber====== 999999:${formatNumber("999999")}")
        println("formatNumber======1000000:${formatNumber("1000000")}")
        println("formatNumber======1000001:${formatNumber("1000001")}")
        println("formatNumber======1100001:${formatNumber("1100001")}")
        println("formatNumber======1010001:${formatNumber("1010001")}")
        //println("formatNumber======123:${formatNumber("")}")
        //println("formatNumber======123:${formatNumber("")}")
    }

    fun formatNumber(strNum: String?): String? {
        val num = strNum?.toLongOrNull()
        if (num != null && num >= 1000000) {
            return strNum.substring(0, strNum.length - 4) + "w"
        }
        return strNum
    }
}