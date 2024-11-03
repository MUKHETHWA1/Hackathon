package com.example.hackathon

import android.app.AlertDialog
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.random.Random

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Cart.newInstance] factory method to
 * create an instance of this fragment.
 */
class Cart : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var cartRecyclerView: RecyclerView
    private lateinit var checkoutButton: Button
    private lateinit var cartItemAdapter: CartItemAdapter
    private val cartItems = mutableListOf<CartItem>() // Sample data list
    private lateinit var totalAmountTextView: TextView
    private lateinit var emptyCartMessage: TextView
    private lateinit var database: FirebaseDatabase
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_cart, container, false)
        cartRecyclerView = view.findViewById(R.id.cartRecyclerView)
        checkoutButton = view.findViewById(R.id.checkoutButton)
        totalAmountTextView = view.findViewById(R.id.totalAmountTextView)
        emptyCartMessage = view.findViewById(R.id.emptyCartMessage)

        // Load items from CartManager
        cartItems.clear()
        cartItems.addAll(CartManager.getCartItems())

        cartItemAdapter = CartItemAdapter(cartItems)
        cartRecyclerView.layoutManager = LinearLayoutManager(context)
        cartRecyclerView.adapter = cartItemAdapter
        // Update total amount
        updateTotalAmount()
        updateEmptyCartMessage()

        setupSwipeToDelete()

        // Initialize Firebase Authentication and Database
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()

        /*val cartItems = mutableListOf<CartItem>()
        val itemDescriptions = cartItems.map {it.name,it.}*/

        checkoutButton.setOnClickListener {
            // Handle checkout logic
            showCheckoutDialog()
        }

        return view
    }

    private val dbRef: DatabaseReference = FirebaseDatabase.getInstance().getReference("Orders")
    private val userId = FirebaseAuth.getInstance().currentUser?.uid

    ///Code to Store orders to firebase
    private fun addOrderToFirebase(orderNumber: Int, creationTime: String, collectionTime: String, totalAmount: Double) {
        if (userId != null) {
            // Structure data for the order
            val orderData = cartItems.map { cartItem ->
                mapOf(
                    "orderNumber" to orderNumber,
                    "creationTime" to creationTime,
                    "collectionTime" to collectionTime,
                    "name" to cartItem.name,
                    "unitPrice" to cartItem.unitPrice,
                    "quantity" to cartItem.quantity,
                    "totalAmount" to totalAmount
                )
            }


            // Push order under "Orders/{userId}/" node
            dbRef.child(userId).push().setValue(orderData)
                .addOnSuccessListener {
                    Toast.makeText(requireContext(), "Order added successfully", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { e ->
                    Log.w("OrderDebug", "Error adding order", e)
                    Toast.makeText(requireContext(), "Failed to add order", Toast.LENGTH_SHORT).show()
                }
        } else {
            Toast.makeText(requireContext(), "User not logged in", Toast.LENGTH_SHORT).show()
        }
    }

    //Code to show checkout alert
    private fun showCheckoutDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Order Placed")

        // Create a TextView for displaying the countdown
        val countdownTextView = TextView(requireContext())
        builder.setView(countdownTextView)

        builder.setCancelable(false)

        // Generate a random order number before starting the countdown
        val orderNumber = Random.nextInt(10000, 99999)

        // Variable to hold the countdown timer
        var countdownTimer: CountDownTimer? = null

        // Cancel button
        builder.setNegativeButton("Cancel Order") { dialog, _ ->
            countdownTimer?.cancel() // Cancel the countdown timer if it is running
            dialog.dismiss() // Dismiss the dialog
            Toast.makeText(requireContext(), "Order cancelled", Toast.LENGTH_SHORT).show()
        }

        val dialog = builder.create()
        dialog.show()

        // Calculate the total amount from cart items
        val totalAmount = cartItems.sumOf { it.unitPrice * it.quantity }

        // Start a countdown timer for 5 minutes (300,000 milliseconds)
        // countdownTimer = object : CountDownTimer(5 * 6o * 1000, 1000) {
        countdownTimer = object : CountDownTimer(1 * 10 * 1000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val minutes = (millisUntilFinished / 1000) / 60
                val seconds = (millisUntilFinished / 1000) % 60
                countdownTextView.text = "Your order will be ready in 5 minutes.\n              $minutes:${String.format("%02d", seconds)}\nUse Any payment method at the till"
            }

            override fun onFinish() {
                dialog.dismiss()
                showOrderReadyDialog(orderNumber, totalAmount) // Pass the order number and total amount
            }
        }.start()
    }
    private fun updateEmptyCartMessage() {
        if (cartItems.isEmpty()) {
            emptyCartMessage.visibility = View.VISIBLE
            cartRecyclerView.visibility = View.GONE
        } else {
            emptyCartMessage.visibility = View.GONE
            cartRecyclerView.visibility = View.VISIBLE
        }
    }
    // Function to calculate and display the total amount
    private fun updateTotalAmount() {
        //val totalAmount = cartItems.sumOf { it.totalPrice }
        val totalAmount = cartItems.sumOf { it.unitPrice * it.quantity }
        totalAmountTextView.text = "Total: R$totalAmount"
    }

    private fun showOrderReadyDialog(orderNumber: Int, totalAmount: Double) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Order Ready")


        // Get Creation and collection times when order is created and collected
        val creationTime = getCurrentTime()
        val collectionTime = getCurrentTime()

// Add order to Firebase Realtime Database
        addOrderToFirebase(orderNumber, creationTime, collectionTime, totalAmount)

        builder.setCancelable(false)

        // Log the order number
        Log.d("OrderDebug", "Creating RecentOrder with number: $orderNumber")

        // Create a new RecentOrder instance
        val recentOrder = RecentOrder(orderNumber, creationTime, getCurrentTime(), totalAmount)
        RecentOrderManager.addRecentOrder(recentOrder)
        builder.setMessage("Your order is ready for collection.\nOrder Number: #$orderNumber\nPay R$totalAmount at the till")

        // Collected button
        builder.setPositiveButton("Done.") { dialog, _ ->
            dialog.dismiss()
            // Capture the collection time
            val collectionTime = getCurrentTime()

            // Create a new RecentOrder instance and add it to RecentOrderManager
            val recentOrder = RecentOrder(orderNumber, creationTime, collectionTime, totalAmount)
            RecentOrderManager.addRecentOrder(recentOrder)


            Toast.makeText(requireContext(), "Order collected", Toast.LENGTH_SHORT).show()
            CartManager.clearCart() // Clear cart after collection
        }

        builder.create().show()
    }

    // Function to get current date and time as a formatted string
    private fun getCurrentTime(): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        return dateFormat.format(Date())
    }



    private fun setupSwipeToDelete() {
        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val removedItem = cartItems[position]
                updateTotalAmount()

                // Remove item from cart
                cartItems.removeAt(position)
                cartItemAdapter.notifyItemRemoved(position)

                // Show removal notification
                Toast.makeText(requireContext(), "${removedItem.name} removed from cart", Toast.LENGTH_SHORT).show()
            }
        }

        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(cartRecyclerView)
    }
}
