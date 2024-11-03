package com.example.hackathon

import android.util.Log

object RecentOrderManager {
    private val recentOrders = mutableListOf<RecentOrder>()


    fun addRecentOrder(order: RecentOrder) {
        recentOrders.add(order)
        Log.d("OrderDebug", "Added RecentOrder: $order")
        Log.d("OrderDebug", "Current Recent Orders List: $recentOrders")
    }

    fun getRecentOrders(): List<RecentOrder> = recentOrders.toList() // Return immutable copy
}