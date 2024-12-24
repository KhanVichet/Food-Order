package com.example.foodapp.Activity;

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.foodapp.Adapter.CategoryAdapter
import com.example.foodapp.Adapter.RecommendedAdapter
import com.example.foodapp.Domain.CategoryDomain
import com.example.foodapp.Domain.FoodDomain
import com.example.foodapp.R
import com.example.foodapp.databinding.ActivityMainBinding
import com.google.android.play.core.integrity.bd
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {
    private val binding : ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    private lateinit var adapter: RecyclerView.Adapter<*>
    private lateinit var adapter2: RecyclerView.Adapter<*>
    private lateinit var recyclerViewCategoryList: RecyclerView
    private lateinit var recyclerViewPopularList: RecyclerView
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        recyclerViewCategory();
        recyclerViewPopular();
        bottomNavigation();



        fetchUserName{userName->
            val Name = findViewById<TextView>(R.id.txtUserName)
            Name.text = "Hi ${userName ?: "Guest"}"

        }


    }

    private fun bottomNavigation() {
        val homeBtn = findViewById<LinearLayout>(R.id.homeBtn)
        val cartBtn = findViewById<LinearLayout>(R.id.cartBtn)



        homeBtn.setOnClickListener {
            startActivity(
                Intent(
                    this@MainActivity,
                    MainActivity::class.java
                )
            )
        }

        cartBtn.setOnClickListener {
            startActivity(
                Intent(
                    this@MainActivity,
                    CartActivity::class.java
                )
            )
        }
    }
    private fun fetchUserName(callback: (String?) -> Unit) {
        val currentUserId = auth.currentUser?.uid
        if (currentUserId == null) {
            Log.e("MainActivity", "User not authenticated.")
            callback(null)
            return
        }

        firestore.collection("users").document(currentUserId).get()
            .addOnSuccessListener { document ->
                val userName = document.getString("name")
                if (userName != null) {
                    callback(userName)
                } else {
                    Log.e("MainActivity", "User document not found.")
                    callback(null)
                }
            }
            .addOnFailureListener { exception ->
                Log.e("MainActivity", "Error fetching user profile: ${exception.message}")
                callback(null)
            }
    }

    private fun recyclerViewPopular() {
        val linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerViewPopularList = findViewById(R.id.recommendedView)
        recyclerViewPopularList.setLayoutManager(linearLayoutManager);



        val foodList = ArrayList<FoodDomain>()
        foodList.add(FoodDomain("Pepperoni pizza", "pizza1", "Slices of pepperoni, mozzarella cheese, fresh oregano, ground black pepper, pizza sauce",13.0,1,5,20,1000));
        foodList.add(FoodDomain("Cheese Burger", "burger", "beef, Gouda Cheese, Special sauce, Lettuce, tomato",15.20,2,4,18,1500));
        foodList.add(FoodDomain("Vegetable pizza", "pizza3", "olive oil, Vegetable oil, pitted kalamata, cherry tomatoes, fresh oregano, basil",11.0,3,3,16,800));

        val adapter2 = RecommendedAdapter(foodList)
        recyclerViewPopularList.setAdapter(adapter2)
    }




    private fun recyclerViewCategory() {
        val linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerViewCategoryList = findViewById(R.id.view1)
        recyclerViewCategoryList.layoutManager = linearLayoutManager

        // Corrected List initialization and object creation
        val categoryList = ArrayList<CategoryDomain>()
        categoryList.add(CategoryDomain("cat_1", "Pizza"));
        categoryList.add(CategoryDomain("cat_2", "Burger"));
        categoryList.add(CategoryDomain("cat_3", "Hotdog"));
        categoryList.add(CategoryDomain("cat_4", "Drink"));
        categoryList.add(CategoryDomain("cat_5", "Donut"));

        val adapter = CategoryAdapter(categoryList);
        recyclerViewCategoryList.setAdapter(adapter)
    }
}

private fun RecyclerView.setAdapter(adapter2: Unit) {

}