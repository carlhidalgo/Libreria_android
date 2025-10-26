package com.example.libreria

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.snackbar.Snackbar
import com.example.libreria.model.Book

class BookDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_detail_new)

        // Setup toolbar with back button
        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener {
            // use finish to return
            finish()
        }

        val ivCover: ImageView = findViewById(R.id.ivDetailCover)
        val tvTitle: TextView = findViewById(R.id.tvDetailTitle)
        val tvDesc: TextView = findViewById(R.id.tvDetailDescription)
        val btnAdd: Button = findViewById(R.id.btnDetailAdd)

        val title = intent?.getStringExtra("title") ?: ""
        val description = intent?.getStringExtra("description") ?: ""
        val imageUrl = intent?.getStringExtra("imageUrl")
        val author = intent?.getStringExtra("author")
        val year = intent?.getIntExtra("year", 0)
        val key = intent?.getStringExtra("key")
        val imageRes = intent?.getIntExtra("imageRes", 0) ?: 0

        tvTitle.text = title
        
        // Build extended description with formatting
        val fullDescription = buildString {
            if (!author.isNullOrBlank()) {
                append("📚 Autor: $author\n\n")
            }
            if (year != null && year > 0) {
                append("📅 Año de publicación: $year\n\n")
            }
            append("📖 Información:\n")
            append(description)
        }
        tvDesc.text = fullDescription
        
        // Load image from URL with Glide (use thumbnail + override for performance)
        if (!imageUrl.isNullOrEmpty()) {
            Glide.with(this)
                .load(imageUrl)
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .override(600)
                .centerCrop()
                // avoid deprecated thumbnail API; rely on placeholder and crossfade for performance
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(ivCover)
        } else {
            if (imageRes != 0) {
                ivCover.setImageResource(imageRes)
            } else {
                val imageRes2 = intent?.getIntExtra("imageRes", R.mipmap.ic_launcher) ?: R.mipmap.ic_launcher
                ivCover.setImageResource(imageRes2)
            }
        }

        // Create a Book instance to add to cart
        val currentBook = Book(
            title = title,
            description = description,
            imageRes = imageRes,
            imageUrl = imageUrl,
            author = author,
            year = if (year != null && year > 0) year else null,
            key = key
        )

        btnAdd.setOnClickListener { view ->
            // Add to central cart manager
            CartManager.add(currentBook)

            // Show lightweight confirmation with action to view cart
            Snackbar.make(view, getString(R.string.added_to_cart, title), Snackbar.LENGTH_LONG)
                .setAction(getString(R.string.view_cart)) {
                    startActivity(android.content.Intent(this@BookDetailActivity, CartActivity::class.java))
                }
                .show()

            // Provide quick feedback and prevent double clicks briefly
            btnAdd.isEnabled = false
            btnAdd.postDelayed({ btnAdd.isEnabled = true }, 600L)
        }
    }
}