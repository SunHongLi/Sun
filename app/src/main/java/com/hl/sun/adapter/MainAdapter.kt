package com.hl.sun.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hl.sun.R
import com.hl.sun.bean.MainRvBean

class MainAdapter() : RecyclerView.Adapter<MainHolder>() {
    private var mDatas = mutableListOf<MainRvBean>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        var view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.main_holder_item, parent, false)
        return MainHolder(view)
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        holder.bindDatas(position,mDatas[position])
    }


    override fun getItemCount(): Int {
        return mDatas.size
    }

    fun updateDatas(list: List<MainRvBean>) {
        mDatas.addAll(list)
        notifyDataSetChanged()
    }
}

