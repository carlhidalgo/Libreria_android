package com.example.libreria.api

import com.google.gson.annotations.SerializedName

// Response from Open Library Search API
data class OpenLibraryResponse(
    @SerializedName("numFound")
    val numFound: Int,
    @SerializedName("start")
    val start: Int,
    @SerializedName("docs")
    val docs: List<BookDoc>
)

// Individual book document from API
data class BookDoc(
    @SerializedName("key")
    val key: String?,
    @SerializedName("title")
    val title: String?,
    @SerializedName("author_name")
    val authorName: List<String>?,
    @SerializedName("first_publish_year")
    val firstPublishYear: Int?,
    @SerializedName("cover_i")
    val coverId: Int?,
    @SerializedName("isbn")
    val isbn: List<String>?,
    @SerializedName("publisher")
    val publisher: List<String>?,
    @SerializedName("language")
    val language: List<String>?,
    @SerializedName("number_of_pages_median")
    val numberOfPages: Int?
)
