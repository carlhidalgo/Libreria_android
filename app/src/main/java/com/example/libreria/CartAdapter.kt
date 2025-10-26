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
import com.google.android.material.imageview.ShapeableImageView

class CartAdapter(
    private val onRemoveClick: (CartManager.CartItem) -> Unit,
    // Request to remove all: includes adapter position so the activity can animate
    private val onRemoveAllRequest: (CartManager.CartItem, Int) -> Unit
) : ListAdapter<CartManager.CartItem, CartAdapter.CartViewHolder>(CartDiffCallback()) {

    class CartViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivCover: ImageView = view.findViewById(R.id.ivCartCover)
        val tvTitle: TextView = view.findViewById(R.id.tvCartTitle)
        val tvAuthor: TextView = view.findViewById(R.id.tvCartAuthor)
        val tvQty: TextView = view.findViewById(R.id.tvCartQty)
        val btnDelete: ShapeableImageView? = view.findViewById(R.id.btnDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_cart, parent, false)
        return CartViewHolder(view)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val item = getItem(position)
        val book = item.book
        holder.itemView.alpha = 1f // reset any animated state
        holder.tvTitle.text = book.title
        holder.tvAuthor.text = book.author ?: ""
        holder.tvQty.text = holder.itemView.context.getString(R.string.qty_format, item.quantity)

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

        // Keep tap to select behavior but now use explicit delete button for removal
        holder.itemView.setOnClickListener {
            // tap to remove one unit
            onRemoveClick(item)
        }

        holder.btnDelete?.setOnClickListener {
            // request deletion; activity will confirm then perform removal (so it can animate)
            val pos = holder.bindingAdapterPosition
            if (pos != RecyclerView.NO_POSITION) {
                onRemoveAllRequest(item, pos)
            } else {
                // fallback: if position invalid, request without position
                onRemoveAllRequest(item, -1)
            }
        }

        holder.itemView.setOnLongClickListener {
            // long press to remove all units as well
            val pos = holder.bindingAdapterPosition
            onRemoveAllRequest(item, if (pos != RecyclerView.NO_POSITION) pos else -1)
            true
        }
    }
}

class CartDiffCallback : DiffUtil.ItemCallback<CartManager.CartItem>() {
    override fun areItemsTheSame(oldItem: CartManager.CartItem, newItem: CartManager.CartItem): Boolean {
        val oldId = oldItem.book.key ?: oldItem.book.title
        val newId = newItem.book.key ?: newItem.book.title
        return oldId == newId
    }

    override fun areContentsTheSame(oldItem: CartManager.CartItem, newItem: CartManager.CartItem): Boolean = oldItem == newItem
}