package com.example.hackathon

import java.io.Serializable

data class Item(
    val name: String,
    val price: Double,
    val imageResourceId: Int
) : Serializable