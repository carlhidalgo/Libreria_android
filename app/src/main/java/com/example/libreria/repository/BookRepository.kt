package com.example.libreria.repository

import com.example.libreria.api.BookDoc
import com.example.libreria.api.RetrofitClient
import com.example.libreria.model.Book

class BookRepository {
    
    private val api = RetrofitClient.api
    
    // Search books with general query
    suspend fun searchBooks(query: String, limit: Int = 20): Result<List<Book>> {
        return try {
            val response = api.searchBooks(query = query, limit = limit)
            val books = response.docs.mapNotNull { doc ->
                convertToBook(doc)
            }
            Result.success(books)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    // Get popular/default books
    suspend fun getPopularBooks(): Result<List<Book>> {
        return try {
            // Search for popular topics to get interesting books
            val response = api.searchBooks(
                query = "subject:fiction OR subject:classics",
                limit = 20
            )
            val books = response.docs.mapNotNull { doc ->
                convertToBook(doc)
            }
            Result.success(books)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    // Convert API BookDoc to our Book model
    private fun convertToBook(doc: BookDoc): Book? {
        val title = doc.title ?: return null
        val author = doc.authorName?.firstOrNull() ?: "Autor desconocido"
        val year = doc.firstPublishYear
        val description = buildDescription(doc)
        val imageUrl = if (doc.coverId != null) {
            Book.getCoverUrl(doc.coverId)
        } else null
        
        return Book(
            title = title,
            description = description,
            author = author,
            year = year,
            imageUrl = imageUrl,
            key = doc.key
        )
    }
    
    // Build description from available data
    private fun buildDescription(doc: BookDoc): String {
        val parts = mutableListOf<String>()
        
        doc.authorName?.firstOrNull()?.let { author ->
            parts.add("Autor: $author")
        }
        
        doc.firstPublishYear?.let { year ->
            parts.add("Publicado en: $year")
        }
        
        doc.publisher?.firstOrNull()?.let { publisher ->
            parts.add("Editorial: $publisher")
        }
        
        doc.numberOfPages?.let { pages ->
            parts.add("Páginas: $pages")
        }
        
        return if (parts.isEmpty()) {
            "Información no disponible"
        } else {
            parts.joinToString(" | ")
        }
    }
}
