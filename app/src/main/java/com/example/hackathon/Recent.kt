package com.example.hackathon

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Recent.newInstance] factory method to
 * create an instance of this fragment.
 */
class Recent : Fragment() {
    private lateinit var recentRecyclerView: RecyclerView
    private lateinit var recentOrderAdapter: RecentOrderAdapter
    private val recentOrders = mutableListOf<RecentOrder>() // List to hold recent orders

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_recent, container, false)
        recentRecyclerView = view.findViewById(R.id.recentRecyclerView)

        // Load recent orders (you should implement a method to get this data)
        recentOrders.clear()
        recentOrders.addAll(RecentOrderManager.getRecentOrders()) // Load recent orders

        recentOrderAdapter = RecentOrderAdapter(recentOrders)
        recentRecyclerView.layoutManager = LinearLayoutManager(context)
        recentRecyclerView.adapter = recentOrderAdapter

        return view
    }
}
