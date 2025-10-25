package com.example.libreria

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.libreria.model.Book
import com.example.libreria.repository.BookRepository
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    private val repository = BookRepository()

    private val _books = MutableLiveData<List<Book>>(emptyList())
    val books: LiveData<List<Book>> = _books

    private val _loading = MutableLiveData(false)
    val loading: LiveData<Boolean> = _loading

    private val _error = MutableLiveData<String?>(null)
    val error: LiveData<String?> = _error

    init {
        loadPopularBooks()
    }

    fun loadPopularBooks() {
        _loading.value = true
        _error.value = null
        viewModelScope.launch {
            try {
                val result = repository.getPopularBooks()
                if (result.isSuccess) {
                    _books.value = result.getOrNull() ?: emptyList()
                } else {
                    _error.value = result.exceptionOrNull()?.message ?: "Error desconocido"
                }
            } catch (e: Exception) {
                _error.value = e.message ?: "Error de red"
            } finally {
                _loading.value = false
            }
        }
    }

    fun search(query: String) {
        _loading.value = true
        _error.value = null
        viewModelScope.launch {
            try {
                val result = repository.searchBooks(query)
                if (result.isSuccess) {
                    _books.value = result.getOrNull() ?: emptyList()
                } else {
                    _error.value = result.exceptionOrNull()?.message ?: "Error desconocido"
                }
            } catch (e: Exception) {
                _error.value = e.message ?: "Error de red"
            } finally {
                _loading.value = false
            }
        }
    }
}