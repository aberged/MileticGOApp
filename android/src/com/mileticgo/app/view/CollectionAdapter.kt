package com.mileticgo.app.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mileticgo.app.CityPin
import com.mileticgo.app.databinding.CollectionItemBinding

class CollectionAdapter(private val onItemClick: (CityPin) -> Unit) : RecyclerView.Adapter<CollectionAdapter.MyViewHolder>() {
    private var mItemsList : List<CityPin> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = CollectionItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = mItemsList[position]
        holder.binding.tvItemTitle.text = item.title

        holder.itemView.setOnClickListener {
            onItemClick(item)
        }
    }

    override fun getItemCount(): Int {
        return mItemsList.size
    }

    class MyViewHolder(val binding: CollectionItemBinding) : RecyclerView.ViewHolder(binding.root)

    fun refreshList(itemsList : List<CityPin>) {
        mItemsList = itemsList
        notifyDataSetChanged()
    }
}