package ru.netology.masterchef.repository

import androidx.lifecycle.LiveData
import ru.netology.masterchef.dto.Recipe


interface RecipeRepository {
    fun get(): LiveData<List<Recipe>>
    fun like(recipeId: Int)
    fun save(recipe: Recipe)
    fun removeByID(recipeId: Int)
    fun selectFavorites(favoriteByMe: Boolean): LiveData<List<Recipe>>
    fun searchDatabase(searchQuery: String?, category: MutableList<String>?): List<Recipe>
}
