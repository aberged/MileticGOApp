package com.mileticgo.app.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mileticgo.app.R

class TopResultAdapter: RecyclerView.Adapter<TopResultAdapter.MyViewHolder>() {

    private lateinit var mItemsList : List<TopResultItem>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.top_result_item, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = mItemsList[position]
        holder.itemTextView.text = item.name
        holder.itemTVPoints.text = item.points.toString()
    }

    override fun getItemCount(): Int {
        return mItemsList.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var itemTextView : TextView = itemView.findViewById(R.id.tv_item_title)
        var itemTVPoints : TextView = itemView.findViewById(R.id.tv_item_points)
    }

    fun refreshList(itemsList : List<TopResultItem>) {
        mItemsList = itemsList
        notifyDataSetChanged()
    }
}