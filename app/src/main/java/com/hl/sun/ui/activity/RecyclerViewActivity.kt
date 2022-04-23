package com.hl.sun.ui.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.hl.sun.R
import kotlinx.android.synthetic.main.activity_recycler_view.*

class RecyclerViewActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recycler_view)


        recyclerView.adapter = MAdapter()
        hIndicator.bindRecyclerView(recyclerView)
    }


    private inner class MAdapter : RecyclerView.Adapter<MViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MViewHolder =
            MViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.item_test,
                    parent,
                    false
                )
            )


        override fun getItemCount(): Int = 15

        override fun onBindViewHolder(holder: MViewHolder, position: Int) {
        }

    }

    private inner class MViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    fun refresh(view: View) {
        pb.refreshPercentAndTxt(listOf(0.2F, 0.3F, 0.5F), listOf("20", "30", "50"))
    }
}