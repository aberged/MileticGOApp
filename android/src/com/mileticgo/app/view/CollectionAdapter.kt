package com.mileticgo.app.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mileticgo.app.CityPin
import com.mileticgo.app.R

class CollectionAdapter(private val onItemClick: (CityPin) -> Unit) : RecyclerView.Adapter<CollectionAdapter.MyViewHolder>() {
    private lateinit var mItemsList : List<CityPin>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.collection_item, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = mItemsList[position]
        holder.itemTextView.text = item.title

        holder.itemView.setOnClickListener {
            onItemClick(item)
        }
    }

    override fun getItemCount(): Int {
        return mItemsList.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var itemTextView : TextView = itemView.findViewById(R.id.tv_item_title)
    }

    fun refreshList(itemsList : List<CityPin>) {
        mItemsList = itemsList
        notifyDataSetChanged()
    }

    public fun setOnItemClickListener() {

    }
}