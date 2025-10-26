package com.example.libreria

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel
    private lateinit var adapter: BookAdapter
    private var tvCartBadge: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_list)

        viewModel = ViewModelProvider(this)[MainViewModel::class.java]

        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)
        recyclerView.setItemViewCacheSize(20)

        adapter = BookAdapter(onItemClick = { book ->
            val intent = Intent(this, BookDetailActivity::class.java).apply {
                putExtra("title", book.title)
                putExtra("description", book.description)
                putExtra("imageUrl", book.imageUrl)
                putExtra("author", book.author)
                putExtra("year", book.year)
                putExtra("key", book.key)
                putExtra("imageRes", book.imageRes)
            }
            startActivity(intent)
        }, onAddClick = { book ->
            CartManager.add(book)
            Toast.makeText(this, "${book.title} añadido al carrito", Toast.LENGTH_SHORT).show()
        })

        recyclerView.adapter = adapter

        val swipeRefresh = findViewById<androidx.swiperefreshlayout.widget.SwipeRefreshLayout>(R.id.swipeRefresh)
        swipeRefresh.setOnRefreshListener {
            viewModel.loadPopularBooks()
        }

        // Observe ViewModel - use submitList to leverage ListAdapter diffing
        viewModel.books.observe(this) { list ->
            adapter.submitList(list)
        }

        viewModel.loading.observe(this) { isLoading ->
            val loadingLayout = findViewById<View>(R.id.loadingLayout)
            if (isLoading) {
                loadingLayout.visibility = View.VISIBLE
                swipeRefresh.isRefreshing = true
            } else {
                loadingLayout.visibility = View.GONE
                swipeRefresh.isRefreshing = false
            }
        }

        viewModel.error.observe(this) { errorMsg ->
            val errorLayout = findViewById<View>(R.id.errorLayout)
            val emptyLayout = findViewById<View>(R.id.emptyLayout)
            if (errorMsg != null) {
                errorLayout.visibility = View.VISIBLE
                emptyLayout.visibility = View.GONE
                val tvError = findViewById<TextView>(R.id.tvError)
                tvError.text = errorMsg
            } else {
                errorLayout.visibility = View.GONE
            }
        }

        // btnRetry
        findViewById<com.google.android.material.button.MaterialButton>(R.id.btnRetry).setOnClickListener {
            viewModel.loadPopularBooks()
        }

        // fabSearch triggers a simple query prompt (placeholder)
        findViewById<com.google.android.material.floatingactionbutton.FloatingActionButton>(R.id.fabSearch).setOnClickListener {
            // For now, trigger a sample search
            viewModel.search("tolkien")
        }

        // Observe cart and update toolbar subtitle with count
        CartManager.items.observe(this) { cartItems ->
            val count = CartManager.totalCount()
            supportActionBar?.subtitle = if (count > 0) "$count en carrito" else ""
            updateBadge(count)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        val cartItem = menu?.findItem(R.id.action_cart)
        val actionView = cartItem?.actionView
        tvCartBadge = actionView?.findViewById(R.id.tvCartBadge)
        actionView?.setOnClickListener {
            onOptionsItemSelected(cartItem)
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_cart -> {
                startActivity(Intent(this, CartActivity::class.java))
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun updateBadge(count: Int) {
        if (count > 0) {
            tvCartBadge?.visibility = View.VISIBLE
            tvCartBadge?.text = count.toString()
        } else {
            tvCartBadge?.visibility = View.GONE
        }
    }
}