package com.example.hackathon

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RecentOrderAdapter(
    private val orders: List<RecentOrder>
) : RecyclerView.Adapter<RecentOrderAdapter.RecentOrderViewHolder>() {

    inner class RecentOrderViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val orderNumberTextView: TextView = view.findViewById(R.id.orderNumberTextView)
        val creationTimeTextView: TextView = view.findViewById(R.id.creationTimeTextView)
        val collectionTimeTextView: TextView = view.findViewById(R.id.collectionTimeTextView)
        val totalAmountTextView: TextView = view.findViewById(R.id.totalAmountTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecentOrderViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recent_order_item, parent, false)
        return RecentOrderViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecentOrderViewHolder, position: Int) {
        val order = orders[position]
        Log.d("OrderDebug", "Binding RecentOrder at position $position: $order")
        holder.orderNumberTextView.text = "Order Number: #${order.orderNumber}"
        holder.creationTimeTextView.text = "Created At: ${order.creationTime}"
        holder.collectionTimeTextView.text = "Collected At: ${order.collectionTime}"
        holder.totalAmountTextView.text = "Total: R${order.totalAmount}"
    }

    override fun getItemCount(): Int = orders.size
}
