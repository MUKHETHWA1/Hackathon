package com.example.hackathon
//item on cart
data class CartItem(
    val name: String,
    val unitPrice: Double,
    var quantity: Int
) {
    val totalPrice: Double
        get() = unitPrice * quantity
}

object CartManager {
    private val cartItems = mutableListOf<CartItem>()

    fun addItemToCart(cartItem: CartItem) {
        cartItems.add(cartItem)
    }

    fun getCartItems(): List<CartItem> {
        return  cartItems
    }

    fun removeItem(cartItem: CartItem) {
        cartItems.remove(cartItem)
    }
    fun clearCart() {
        cartItems.clear()
    }
}