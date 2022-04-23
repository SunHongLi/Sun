package com.hl.sun.ui.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.hl.sun.R

/**
 * Function:底部tab
 * Date:2021/9/3
 * Author: sunHL
 */
class BottomTabView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attributeSet, defStyleAttr), View.OnClickListener {
    private val tabCount = 3

    //图标imageview
    private var tabIvs = mutableListOf<ImageView>()

    //tab TextView
    private var tabTvs = mutableListOf<TextView>()

    //角标TextView
    private var tabBadgeTvs = mutableListOf<TextView>()

    //图标container
    private var tabIds = arrayOf(R.id.bottom_tab_id_0, R.id.bottom_tab_id_1, R.id.bottom_tab_id_2)

    //图标imageview的id
//    private var tabIvIds =
//        arrayOf(R.id.bottom_tab_iv_id_0, R.id.bottom_tab_iv_id_1, R.id.bottom_tab_iv_id_2)

    //未选中图标
    private var icons = arrayOf(R.mipmap.tab_my, R.mipmap.tab_my, R.mipmap.tab_my)

    //选中图标
    private var selectIcons =
        arrayOf(R.mipmap.tab_my_selected, R.mipmap.tab_my_selected, R.mipmap.tab_my_selected)

    //tab名称
    private var tabName = arrayOf("背词", "词库", "设置")

    //文字选中颜色
    private var tvSelectedColor = ContextCompat.getColor(context, R.color.tomato)

    //文字未选中颜色
    private var tvUnselectedColor = ContextCompat.getColor(context, R.color.gray)

    private var currentTabIndex = -1

    init {
        addTabs()
        selectedTab(0)
    }

    private fun selectedTab(index: Int) {
        changeTabStatus(index)
        if (currentTabIndex != index) {
            onTabSelectedListener?.onTabSelected(index)
        } else {
            onTabSelectedListener?.onTabReselect(index)
        }
        currentTabIndex = index
    }

    private fun changeTabStatus(index: Int) {
        for (i in 0 until tabCount) {
            tabIvs[i].setImageResource(
                if (i == index) {
                    selectIcons[i]
                } else {
                    icons[i]
                }
            )
            tabTvs[i].setTextColor(
                if (i == index) {
                    tvSelectedColor
                } else {
                    tvUnselectedColor
                }
            )
        }
    }

    private fun addTabs() {
        for (i in 0 until tabCount) {
            var tabView =
                LayoutInflater.from(context).inflate(R.layout.bottom_tab_item, this, false)
            tabView.id = tabIds[i]
            tabView.layoutParams =
                LayoutParams(0, LayoutParams.WRAP_CONTENT, 1f)

            //icon
            var iv = tabView.findViewById<ImageView>(R.id.bottom_tab_iv)
            iv.setImageResource(icons[i])
            tabIvs.add(i, iv)

            //tab名称
            var tv = tabView.findViewById<TextView>(R.id.bottom_tab_tv)
            tv.text = tabName[i]
            tabTvs.add(i, tv)

            //角标
            var badge = tabView.findViewById<TextView>(R.id.bottom_tab_badge_tv)
            badge.visibility = View.INVISIBLE
            tabBadgeTvs.add(i, badge)

            //点击事件
            tabView.setOnClickListener(this)

            addView(tabView)
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            tabIds[0] -> {
                //背词
                selectedTab(0)
            }
            tabIds[1] -> {
                //词库
                selectedTab(1)
            }
            tabIds[2] -> {
                //设置
                selectedTab(2)
            }
        }
    }

    var onTabSelectedListener: OnTabSelectedListener? = null
        set(value) {
            field = value
            onTabSelectedListener?.onTabSelected(0)
        }

    interface OnTabSelectedListener {
        fun onTabSelected(index: Int)
        fun onTabReselect(index: Int)
    }

    fun setReciteWordBadge(count: String) {
        tabBadgeTvs[0].text = count
        tabBadgeTvs[0].visibility = View.VISIBLE
    }

    fun hideReciteWordBadge() {
        tabBadgeTvs[0].text = ""
        tabBadgeTvs[0].visibility = View.INVISIBLE
    }
}