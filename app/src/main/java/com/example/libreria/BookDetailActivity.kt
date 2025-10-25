package com.example.libreria

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.android.material.appbar.MaterialToolbar

class BookDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_detail_new)

        // Setup toolbar with back button
        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        toolbar.setNavigationOnClickListener {
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
        
        // Load image from URL with Glide
        if (!imageUrl.isNullOrEmpty()) {
            Glide.with(this)
                .load(imageUrl)
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .centerCrop()
                .into(ivCover)
        } else {
            val imageRes = intent?.getIntExtra("imageRes", R.mipmap.ic_launcher) ?: R.mipmap.ic_launcher
            ivCover.setImageResource(imageRes)
        }

        btnAdd.setOnClickListener {
            Toast.makeText(
                this@BookDetailActivity,
                "✓ $title añadido al carrito",
                Toast.LENGTH_SHORT
            ).show()
            finish() // Return to list after adding
        }
    }
}
