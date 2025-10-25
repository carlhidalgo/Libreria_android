package com.example.libreria.api

import retrofit2.http.GET
import retrofit2.http.Query

interface OpenLibraryApi {
    
    @GET("search.json")
    suspend fun searchBooks(
        @Query("q") query: String,
        @Query("fields") fields: String = "key,title,author_name,first_publish_year,cover_i,isbn,publisher,language,number_of_pages_median",
        @Query("limit") limit: Int = 20,
        @Query("offset") offset: Int = 0
    ): OpenLibraryResponse
    
    @GET("search.json")
    suspend fun searchByTitle(
        @Query("title") title: String,
        @Query("fields") fields: String = "key,title,author_name,first_publish_year,cover_i,isbn,publisher,language,number_of_pages_median",
        @Query("limit") limit: Int = 20
    ): OpenLibraryResponse
    
    @GET("search.json")
    suspend fun searchByAuthor(
        @Query("author") author: String,
        @Query("fields") fields: String = "key,title,author_name,first_publish_year,cover_i,isbn,publisher,language,number_of_pages_median",
        @Query("limit") limit: Int = 20
    ): OpenLibraryResponse
}
