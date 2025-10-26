package com.example.libreria

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.libreria.model.Book

/**
 * Simple in-memory cart manager with LiveData to observe changes.
 * Now stores CartItem objects with quantity to avoid duplicates and manage counts.
 */
object CartManager {
    data class CartItem(val book: Book, var quantity: Int = 1)

    private val _items = MutableLiveData<List<CartItem>>(emptyList())
    val items: LiveData<List<CartItem>> = _items

    fun add(book: Book) {
        val current = _items.value?.toMutableList() ?: mutableListOf()
        val existing = current.indexOfFirst { it.book.key == book.key && it.book.title == book.title }
        if (existing >= 0) {
            current[existing].quantity += 1
        } else {
            current.add(CartItem(book, 1))
        }
        _items.value = current
    }

    fun remove(book: Book) {
        val current = _items.value?.toMutableList() ?: mutableListOf()
        val existing = current.indexOfFirst { it.book.key == book.key && it.book.title == book.title }
        if (existing >= 0) {
            val item = current[existing]
            if (item.quantity > 1) {
                item.quantity -= 1
                current[existing] = item
            } else {
                current.removeAt(existing)
            }
        }
        _items.value = current
    }

    fun removeAll(book: Book) {
        val current = _items.value?.toMutableList() ?: mutableListOf()
        current.removeAll { it.book.key == book.key && it.book.title == book.title }
        _items.value = current
    }



    fun totalCount(): Int = _items.value?.sumOf { it.quantity } ?: 0
}