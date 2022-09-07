package ru.netology.masterchef.dto


data class Recipe(
    val id: Int,
    val author: String,
    val title: String,
    val content: String,
    val favoriteByMe: Boolean = false,
    val category: String,
    val imageUrl: String? = null,
)