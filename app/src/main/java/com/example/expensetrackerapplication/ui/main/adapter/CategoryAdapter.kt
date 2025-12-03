package com.example.expensetrackerapplication.ui.main.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.expensetrackerapplication.R


class CategoryAdapter(
    private val items: List<String>
) : RecyclerView.Adapter<CategoryAdapter.PopupVH>() {

    inner class PopupVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvItem: TextView = itemView.findViewById(R.id.tvItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopupVH {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_popup, parent, false)
        return PopupVH(view)
    }

    override fun onBindViewHolder(holder: PopupVH, position: Int) {
        holder.tvItem.text = items[position]

//        holder.itemView.setOnClickListener {
//            onSelect(items[position])
//        }
    }

    override fun getItemCount(): Int = items.size
}
