package com.mileticgo.app.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mileticgo.app.Repository
import com.mileticgo.app.TopScoreListItem
import com.mileticgo.app.databinding.TopResultItemBinding

class TopResultAdapter: RecyclerView.Adapter<TopResultAdapter.MyViewHolder>() {

    private var mItemsList : List<TopScoreListItem> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = TopResultItemBinding.inflate(LayoutInflater.from(parent.context),  parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = mItemsList[position]
        holder.binding.tvPosition.text = item.position.toString()
        if (Repository.get().user.name == item.userName) {
            holder.binding.ivUserIcon.visibility = View.VISIBLE
        } else {
            holder.binding.ivUserIcon.visibility = View.GONE
        }
        holder.binding.tvItemTitle.text = item.userName
        holder.binding.tvItemPoints.text = item.userPoints.toString()
    }

    override fun getItemCount(): Int {
        return mItemsList.size
    }

    class MyViewHolder(val binding: TopResultItemBinding) : RecyclerView.ViewHolder(binding.root)

    fun refreshList(itemsList : List<TopScoreListItem>) {
        mItemsList = itemsList
        notifyDataSetChanged()
    }
}