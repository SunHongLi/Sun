package com.hl.sun.ui.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.hl.sun.R

/**
 * Function:熟悉度tab
 * Date:2021/9/3
 * Author: sunHL
 */
class FamiliarityTab @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attributeSet, defStyleAttr), View.OnClickListener {
    private var currentTabIndex = -1
    private var tabCount = 4

    //文字选中颜色
    private var tvSelectedColor = ContextCompat.getColor(context, R.color.tomato)

    //文字未选中颜色
    private var tvUnselectedColor = ContextCompat.getColor(context, R.color.gray)

    //图标container id
    private var tabIds =
        arrayOf(R.id.incognizance_ll, R.id.indistinct_ll, R.id.know_ll, R.id.mastered_ll)

    //每个tab 容器
    val tabContiners by lazy {
        mutableListOf<ViewGroup>(
            findViewById(R.id.incognizance_ll),
            findViewById(R.id.indistinct_ll),
            findViewById(R.id.know_ll),
            findViewById(R.id.mastered_ll)
        )
    }

    //数量标题
    val countTvs by lazy {
        mutableListOf<TextView>(
            findViewById(R.id.incognizance_count_tv),
            findViewById(R.id.indistinct_count_tv),
            findViewById(R.id.know_count_tv),
            findViewById(R.id.mastered_count_tv)
        )
    }

    //标题
    val titleTvs by lazy {
        mutableListOf<TextView>(
            findViewById(R.id.incognizance_tv),
            findViewById(R.id.indistinct_tv),
            findViewById(R.id.know_tv),
            findViewById(R.id.mastered_tv)
        )
    }

    init {
        addView(
            LayoutInflater.from(context).inflate(R.layout.home_word_familiarity_tab, this, false)
        )
        for (i in 0 until tabCount) {
            tabContiners[i].setOnClickListener(this)
        }

        selectedTab(0)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            tabIds[0] -> {
                //不认识
                selectedTab(0)
            }
            tabIds[1] -> {
                //模糊
                selectedTab(1)
            }
            tabIds[2] -> {
                //认识
                selectedTab(2)
            }
            tabIds[3] -> {
                //已掌握
                selectedTab(3)
            }
        }
    }

    private fun selectedTab(index: Int) {
        changeTabUI(index)
        if (currentTabIndex != index) {
            onTabSelectedListener?.onTabSelected(index)
        } else {
            onTabSelectedListener?.onTabReselect(index)
        }
        currentTabIndex = index
    }

    private fun changeTabUI(index: Int) {
        for (i in 0 until tabCount) {
            tabContiners[i].setBackgroundResource(
                if (i == index) {
                    R.drawable.home_white_tab_bg
                } else {
                    R.drawable.home_gray_tab_bg
                }
            )
            countTvs[i].setTextColor(
                if (i == index) {
                    tvSelectedColor
                } else {
                    tvUnselectedColor
                }
            )
            titleTvs[i].setTextColor(
                if (i == index) {
                    tvSelectedColor
                } else {
                    tvUnselectedColor
                }
            )
        }
    }

    var onTabSelectedListener: OnTabSelectedListener? = null

    interface OnTabSelectedListener {
        fun onTabSelected(index: Int)
        fun onTabReselect(index: Int)
    }

}