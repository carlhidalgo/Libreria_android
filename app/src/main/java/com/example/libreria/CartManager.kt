package com.example.libreria

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.libreria.model.Book

/**
 * Simple in-memory cart manager with LiveData to observe changes.
 */
object CartManager {
    private val _items = MutableLiveData<List<Book>>(emptyList())
    val items: LiveData<List<Book>> = _items

    fun add(book: Book) {
        val current = _items.value ?: emptyList()
        _items.value = current + book
    }

    fun remove(book: Book) {
        val current = _items.value ?: emptyList()
        _items.value = current.filterNot { it.key == book.key && it.title == book.title }
    }

    fun clear() {
        _items.value = emptyList()
    }

    fun count(): Int = _items.value?.size ?: 0
}
