package com.example.hackathon

data class RecentOrder(val orderNumber: Int,
                       val creationTime: String,
                       val collectionTime: String,
                       val totalAmount: Double
)