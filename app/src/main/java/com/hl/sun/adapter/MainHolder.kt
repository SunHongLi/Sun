package com.hl.sun.adapter

import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.hl.sun.R
import com.hl.sun.bean.MainRvBean

class MainHolder(itemView: View) : ViewHolder(itemView) {
    private var titleTv: TextView = itemView.findViewById<TextView>(R.id.title)
    private lateinit var bean: MainRvBean
    private var context: Context = itemView.context

    fun bindDatas(position: Int, mainRvBean: MainRvBean) {
        bean = mainRvBean

        titleTv.text = bean.title
        titleTv.setOnClickListener {
            var intent = Intent(context, bean.clazz)
            context.startActivity(intent)
        }
    }

}
