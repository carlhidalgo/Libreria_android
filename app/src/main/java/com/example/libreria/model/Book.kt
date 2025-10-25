package com.example.libreria.model

data class Book(
    val title: String,
    val description: String,
    val imageRes: Int = 0,
    val imageUrl: String? = null,
    val author: String? = null,
    val year: Int? = null,
    val key: String? = null
) {
    // Helper function to get cover URL from Open Library
    companion object {
        fun getCoverUrl(coverId: Int?, size: String = "M"): String {
            return if (coverId != null) {
                "https://covers.openlibrary.org/b/id/$coverId-$size.jpg"
            } else {
                ""
            }
        }
    }
}
