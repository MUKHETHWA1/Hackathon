package com.example.hackathon

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class DetailActivity : AppCompatActivity() {
    companion object {
        private const val EXTRA_ITEM = "extra_item"

        fun start(context: Context, item: Item) {
            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra(EXTRA_ITEM, item)
            context.startActivity(intent)
        }
    }

    private var quantity = 1
    private var basePrice = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val item = intent.getSerializableExtra(EXTRA_ITEM) as? Item

        val itemImageView: ImageView = findViewById(R.id.detailItemImage)
        val itemNameTextView: TextView = findViewById(R.id.detailItemName)
        val itemPriceTextView: TextView = findViewById(R.id.detailItemPrice)
        val quantityTextView: TextView = findViewById(R.id.itemQuantity)
        val addToCartButton: Button = findViewById(R.id.addToCartButton)

        item?.let {
            basePrice = it.price
            itemImageView.setImageResource(it.imageResourceId)
            itemNameTextView.text = it.name
            updatePrice()
        }

        // Quantity adjustment logic
        findViewById<TextView>(R.id.decreaseQuantity).setOnClickListener {
            if (quantity > 1) {
                quantity--
                quantityTextView.text = quantity.toString()
                updatePrice()
            }
        }

        findViewById<TextView>(R.id.increaseQuantity).setOnClickListener {
            quantity++
            quantityTextView.text = quantity.toString()
            updatePrice()
        }

        // Add to Cart button logic
        addToCartButton.setOnClickListener {
            item?.let {
                val cartItem = CartItem(it.name, it.price, quantity)
                CartManager.addItemToCart(cartItem)
                finish()
            }
        }
    }

    // Update price based on quantity
    private fun updatePrice() {
        val totalPrice = basePrice * quantity
        findViewById<TextView>(R.id.detailItemPrice).text = "R$totalPrice"
    }
}




