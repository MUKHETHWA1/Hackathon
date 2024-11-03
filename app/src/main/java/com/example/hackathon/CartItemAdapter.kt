package com.example.hackathon

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CartItemAdapter(
    private val items: List<CartItem>
) : RecyclerView.Adapter<CartItemAdapter.CartItemViewHolder>() {

    inner class CartItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val itemName: TextView = view.findViewById(R.id.itemName)
        val itemPrice: TextView = view.findViewById(R.id.itemPrice)
        val itemQuantity: TextView = view.findViewById(R.id.itemQuantity)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cart_item_layout, parent, false)
        return CartItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: CartItemViewHolder, position: Int) {
        val item = items[position]
        holder.itemName.text = item.name
        holder.itemPrice.text = "Price: ${item.totalPrice}"
        holder.itemQuantity.text = "Qty: ${item.quantity}"

        // Set up listener for quantity change (this example assumes a button, but a custom quantity input view could work)
        holder.itemQuantity.setOnClickListener {
            item.quantity += 1  // Increase quantity (or set up +/- button logic here)
            notifyItemChanged(position)  // Refresh the item to update price
        }
    }


// Returns the number of items in the cart
    override fun getItemCount(): Int = items.size
}
