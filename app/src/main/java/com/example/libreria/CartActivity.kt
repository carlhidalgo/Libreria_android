package com.example.libreria

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class CartActivity : AppCompatActivity() {

    private lateinit var adapter: CartAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        // Use the toolbar from layout and set close icon behavior
        val toolbar = findViewById<com.google.android.material.appbar.MaterialToolbar>(R.id.toolbar_cart)
        toolbar.navigationIcon = ContextCompat.getDrawable(this, R.drawable.ic_close_black_24dp)
        toolbar.setNavigationOnClickListener {
            finish()
        }

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerCart)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)
        recyclerView.setItemViewCacheSize(20)
        recyclerView.setRecycledViewPool(RecyclerView.RecycledViewPool())

        // disable change animations to reduce flicker on submitList
        (recyclerView.itemAnimator as? DefaultItemAnimator)?.supportsChangeAnimations = false

        adapter = CartAdapter({ cartItem ->
            // remove one unit
            CartManager.remove(cartItem.book)
        }, { cartItem, pos ->
            // Request to remove all units -> show confirmation dialog
            showConfirmAndRemove(cartItem, pos, recyclerView)
        })
        recyclerView.adapter = adapter

        val tvEmpty = findViewById<TextView>(R.id.tvCartEmpty)

        CartManager.items.observe(this, Observer { items ->
            adapter.submitList(items)
            tvEmpty.visibility = if (items.isEmpty()) View.VISIBLE else View.GONE
        })
    }

    private fun showConfirmAndRemove(item: CartManager.CartItem, position: Int, recyclerView: RecyclerView) {
        AlertDialog.Builder(this)
            .setTitle(R.string.oops_error)
            .setMessage(getString(R.string.delete) + "?" )
            .setPositiveButton(R.string.delete) { _, _ ->
                // animate item at position if valid then remove from manager
                if (position >= 0 && position < recyclerView.childCount) {
                    val child = recyclerView.getChildAt(position)
                    child?.animate()?.alpha(0f)?.setDuration(220)?.withEndAction {
                        CartManager.removeAll(item.book)
                    }?.start()
                } else {
                    // fallback: remove immediately
                    CartManager.removeAll(item.book)
                }
            }
            .setNegativeButton(R.string.cancel, null)
            .show()
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}