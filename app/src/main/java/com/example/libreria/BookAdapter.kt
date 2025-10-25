package com.example.libreria

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.google.android.material.button.MaterialButton
import com.example.libreria.model.Book

class BookAdapter(
    private val onItemClick: (Book) -> Unit,
    private val onAddClick: (Book) -> Unit
) : ListAdapter<Book, BookAdapter.BookViewHolder>(BookDiffCallback()) {

    class BookViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivCover: ImageView = view.findViewById(R.id.ivCover)
        val tvTitle: TextView = view.findViewById(R.id.tvTitle)
        val tvDescription: TextView = view.findViewById(R.id.tvDescription)
        val btnAdd: MaterialButton = view.findViewById(R.id.btnAdd)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_book, parent, false)
        return BookViewHolder(view)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        val book = getItem(position)

        holder.tvTitle.text = book.title
        holder.tvDescription.text = book.description

        // Optimize image loading and caching with Glide
        val context = holder.itemView.context
        if (!book.imageUrl.isNullOrEmpty()) {
            Glide.with(context)
                .load(book.imageUrl)
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .centerCrop()
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(holder.ivCover)
        } else if (book.imageRes != 0) {
            holder.ivCover.setImageResource(book.imageRes)
        } else {
            holder.ivCover.setImageResource(R.mipmap.ic_launcher)
        }

        // Improve button visibility: use primary color for icon tint
        holder.btnAdd.iconTint = null
        holder.btnAdd.setTextColor(ContextCompat.getColor(context, R.color.white))
        holder.btnAdd.setBackgroundColor(ContextCompat.getColor(context, R.color.accent))

        holder.itemView.setOnClickListener { onItemClick(book) }

        holder.btnAdd.setOnClickListener { view ->
            // Lightweight feedback
            view.isEnabled = false
            view.postDelayed({ view.isEnabled = true }, 300)
            onAddClick(book)
        }
    }
}

class BookDiffCallback : DiffUtil.ItemCallback<Book>() {
    override fun areItemsTheSame(oldItem: Book, newItem: Book): Boolean {
        val oldId = oldItem.key ?: oldItem.title
        val newId = newItem.key ?: newItem.title
        return oldId == newId
    }
    override fun areContentsTheSame(oldItem: Book, newItem: Book): Boolean = oldItem == newItem
}