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
 * Use the [Menu.newInstance] factory method to
 * create an instance of this fragment.
 */
class Menu : Fragment() , CategoryAdapter.OnCategoryClickListener, ItemAdapter.OnItemClickListener {


    private lateinit var categoryRecyclerView: RecyclerView
    private lateinit var itemRecyclerView: RecyclerView
    private lateinit var itemAdapter: ItemAdapter
    private val itemList = mutableListOf<Item>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_menu, container, false)

        categoryRecyclerView = view.findViewById(R.id.categoryRecyclerView)
        itemRecyclerView = view.findViewById(R.id.itemRecyclerView)

        // Set up category RecyclerView
        val categories = listOf("Sandwiches","Burgers","Pizza", "Cool Drinks", "Coffees", "Sweet Treats")
        val categoryAdapter = CategoryAdapter(categories, this)
        categoryRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        categoryRecyclerView.adapter = categoryAdapter

        // Set up item RecyclerView
        itemAdapter = ItemAdapter(itemList, this)
        itemRecyclerView.layoutManager = LinearLayoutManager(context)
        itemRecyclerView.adapter = itemAdapter

        // Load items initially (default category)
        loadItemsByCategory("Sandwiches")

        return view
    }

    private fun loadItemsByCategory(category: String) {
        itemList.clear()
        when (category) {
            "Sandwiches" -> {
                itemList.add(Item("Sandwich 1", 23.90, R.drawable.sandwich_image1))
                itemList.add(Item("Sandwich 2", 25.00, R.drawable.sandwich_image2))
                itemList.add(Item("Sandwich with cheese", 23.90, R.drawable.sd4_cheese))
                itemList.add(Item("Sandwich 3", 25.00, R.drawable.sd2))
                itemList.add(Item("Sandwich 4", 10.90, R.drawable.sd3))
                itemList.add(Item("Sandwich With Eggs 1", 25.00, R.drawable.sd5_egg))
                itemList.add(Item("Sandwich With Eggs 2", 23.90, R.drawable.sd6_egg2))
                itemList.add(Item("Sandwich With Peanut Butter", 15.00, R.drawable.sd7_butter))
                itemList.add(Item("Sandwich with cheese 2", 23.90, R.drawable.sd8_cheese))
            }
            "Burgers" -> {
                itemList.add(Item("Veggie Burger", 43.90, R.drawable.bg1))
                itemList.add(Item("BBQ Burger", 45.00, R.drawable.bg2))
                itemList.add(Item("Lamb Burger", 43.90, R.drawable.bg3))
                itemList.add(Item("Bacon Burger", 45.00, R.drawable.bg4))
                itemList.add(Item("Veggies Burger 2", 43.90, R.drawable.bg5))
                itemList.add(Item("Petty Burger ", 45.00, R.drawable.bg6))
                itemList.add(Item("Fried Chicken ", 43.90, R.drawable.bg7))
                itemList.add(Item("Petty Burger  2", 45.00, R.drawable.bg8))

            }
            "Pizza" -> {
                itemList.add(Item("Pepperoni Pizza 1", 33.90, R.drawable.p1))
                itemList.add(Item("BBQ Cheesy Pizza", 25.00, R.drawable.p2))
                itemList.add(Item("Chicken Pizza 3", 40.90, R.drawable.p3))
                itemList.add(Item("barbecue-chicken", 45.00, R.drawable.p4))
                itemList.add(Item("Pepperoni Veggies", 43.90, R.drawable.p5))
                itemList.add(Item("BBQ-Chicken ", 40.90, R.drawable.p6))
                itemList.add(Item("Pepperoni", 49.00, R.drawable.p7))
                itemList.add(Item("Veggie Pizza", 33.90, R.drawable.p8))
            }
            "Cool Drinks" -> {
                itemList.add(Item("Cola", 32.90, R.drawable.colacola))
                itemList.add(Item("Lemonade", 30.50, R.drawable.lemonade_image))
                itemList.add(Item("Sprite", 12.90, R.drawable.sprite))
                itemList.add(Item("Tonic", 10.50, R.drawable.tonic))
                itemList.add(Item("7 UP", 10.50, R.drawable.seven))
                itemList.add(Item("Fanta Orange", 12.90, R.drawable.fantaorange))
                itemList.add(Item("Fanta Grape", 10.50, R.drawable.grape))
            }
            "Coffees" -> {
                itemList.add(Item("Cappuccino", 25.00, R.drawable.coffee_image))
                itemList.add(Item("Espresso", 20.00, R.drawable.espresso_image))
                itemList.add(Item("Soy Latte", 35.00, R.drawable.sb1))
                itemList.add(Item("Chocco Frapp", 25.00, R.drawable.sb2))
                itemList.add(Item("Bottled Americano", 45.00, R.drawable.sb3))
                itemList.add(Item("Rainbow Frapp", 30.00, R.drawable.sb4))
                itemList.add(Item("Caramel Frapp", 45.00, R.drawable.sb5))
                itemList.add(Item("Black Forest Frapp", 30.00, R.drawable.sb6))
            }
            "Sweet Treats" -> {
                itemList.add(Item("Chocolate Cake", 40.00, R.drawable.chocolate_cake_image))
                itemList.add(Item("Donut", 10.00, R.drawable.donut_image))
                itemList.add(Item("Muffin 1", 12.00, R.drawable.muffin))
                itemList.add(Item("Muffin 2", 15.00, R.drawable.muffin2))
                itemList.add(Item("Red Muffins ", 15.00, R.drawable.muffin3_red))
                itemList.add(Item("Glutten Treats", 15.00, R.drawable.glutten))
                itemList.add(Item("Sweets Biscuits", 17.00, R.drawable.biscuits))
            }
        }
        itemAdapter.notifyDataSetChanged()
    }

    override fun onCategoryClick(category: String) {
        loadItemsByCategory(category)
    }

    override fun onItemClick(item: Item) {
        context?.let { DetailActivity.start(it, item) }
    }
}
