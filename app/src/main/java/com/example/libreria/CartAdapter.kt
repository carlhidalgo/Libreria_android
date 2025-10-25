package com.example.libreria

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.libreria.model.Book

class CartAdapter(
    private val onRemoveClick: (Book) -> Unit
) : ListAdapter<Book, CartAdapter.CartViewHolder>(CartDiffCallback()) {

    class CartViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivCover: ImageView = view.findViewById(R.id.ivCartCover)
        val tvTitle: TextView = view.findViewById(R.id.tvCartTitle)
        val tvAuthor: TextView = view.findViewById(R.id.tvCartAuthor)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_cart, parent, false)
        return CartViewHolder(view)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val book = getItem(position)
        holder.tvTitle.text = book.title
        holder.tvAuthor.text = book.author ?: ""
        if (!book.imageUrl.isNullOrEmpty()) {
            Glide.with(holder.itemView.context)
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

        holder.itemView.setOnLongClickListener {
            onRemoveClick(book)
            true
        }
    }
}

class CartDiffCallback : DiffUtil.ItemCallback<Book>() {
    override fun areItemsTheSame(oldItem: Book, newItem: Book): Boolean {
        val oldId = oldItem.key ?: oldItem.title
        val newId = newItem.key ?: newItem.title
        return oldId == newId
    }

    override fun areContentsTheSame(oldItem: Book, newItem: Book): Boolean = oldItem == newItem
}
