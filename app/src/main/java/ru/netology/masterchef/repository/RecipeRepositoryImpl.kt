package ru.netology.masterchef.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import ru.netology.masterchef.dto.Recipe
import ru.netology.masterchef.entity.RecipeEntity.Companion.toEntity
import ru.netology.masterchef.entity.RecipeEntity.Companion.toModel
import ru.netology.nmedia.db.RecipeDao

class RecipeRepositoryImpl(private val dao: RecipeDao) : RecipeRepository {

    override fun get(): LiveData<List<Recipe>> = dao.getAll().map { entities ->
        entities.map { it.toModel() }
    }

    override fun like(recipeId: Int) = dao.likeById(recipeId)

    override fun save(recipe: Recipe) = dao.save(recipe.toEntity())

    override fun removeByID(recipeId: Int) = dao.removeById(recipeId)

    override fun searchDatabase(
        searchQuery: String?,
        category: MutableList<String>?,
    ): List<Recipe> {
        return dao.searchDatabase(searchQuery, category).map { entities ->
            entities.toModel()
        }
    }

    override fun selectFavorites(favoriteByMe: Boolean): LiveData<List<Recipe>> {
        return dao.selectFavorites(favoriteByMe).map { list ->
            list.map { it.toModel() }
        }
    }

}


