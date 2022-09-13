package com.hl.sun.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.hl.sun.R
import com.hl.sun.adapter.MainAdapter
import com.hl.sun.bean.MainRvBean
import com.hl.sun.ui.TouchScrollActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val mainAdapter = MainAdapter()
        rv.adapter = mainAdapter
        rv.layoutManager = LinearLayoutManager(this)
        mainAdapter.updateDatas(getDatas())
    }

    private fun getDatas(): List<MainRvBean> {
        var list = mutableListOf<MainRvBean>()
        list.add(0, MainRvBean("工具类测试", UtilsActivity::class.java))
        list.add(0, MainRvBean("触摸滑动页面", TouchScrollActivity::class.java))
        list.add(0, MainRvBean("底部tab展示页面", TabActivity::class.java))
        list.add(0, MainRvBean("动画展示页面", AnimActivity::class.java))
        list.add(0, MainRvBean("横向滚动条RecyclerView", RecyclerViewActivity::class.java))
        list.add(0, MainRvBean("H5静态资源缓存", WebCacheActivity::class.java))

        return list
    }


}