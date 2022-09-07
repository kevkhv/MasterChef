package ru.netology.nmedia.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import ru.netology.masterchef.entity.RecipeEntity


@Dao
interface RecipeDao {
    @Query("SELECT * FROM recipes ORDER BY id DESC")
    fun getAll(): LiveData<List<RecipeEntity>>

    @Query(
        """
        UPDATE recipes SET
        favoriteByMe = CASE WHEN favoriteByMe THEN 0 ELSE 1 END
        WHERE id = :id
        """
    )
    fun likeById(id: Int)

    @Insert
    fun insert(recipe: RecipeEntity)

    fun save(recipe: RecipeEntity) =
        if (recipe.id == 0)
            insert(recipe)
        else updateContentById(recipe.id,
            recipe.content,
            recipe.author,
            recipe.title,
            recipe.category,
            recipe.imageUrl)

    @Query("DELETE FROM recipes WHERE id = :id")
    fun removeById(id: Int)

    @Query(
        """UPDATE recipes SET
         content = :content, 
         author = :author,
         title = :title,
         category = :category,
         imageUrl = :imageUrl
         WHERE id = :id
         """
    )
    fun updateContentById(
        id: Int, content: String, author: String, title: String, category: String,
        imageUrl: String? = null,
    )

    @Query("SELECT * FROM recipes WHERE (title LIKE :searchQuery) AND category IN (:categoryList) ORDER BY id DESC")
    fun searchDatabase(searchQuery: String?, categoryList: MutableList<String>?): List<RecipeEntity>

    @Query("SELECT * FROM recipes WHERE favoriteByMe LIKE :favoriteByMe")
    fun selectFavorites(favoriteByMe: Boolean): LiveData<List<RecipeEntity>>
}