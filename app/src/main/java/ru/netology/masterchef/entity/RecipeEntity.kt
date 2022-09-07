package ru.netology.masterchef.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.netology.masterchef.dto.Recipe

@Entity(tableName = "recipes")
class RecipeEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int,
    val author: String,
    val title: String,
    val content: String,

    @ColumnInfo(name = "favoriteByMe")
    val favoriteByMe: Boolean,
    val category: String,
    val imageUrl: String? = null,
) {

    companion object {
        internal fun RecipeEntity.toModel() = Recipe(
            id = id,
            author = author,
            title = title,
            content = content,
            favoriteByMe = favoriteByMe,
            category = category,
            imageUrl = imageUrl
        )

        internal fun Recipe.toEntity() = RecipeEntity(
            id = id,
            author = author,
            title = title,
            content = content,
            favoriteByMe = favoriteByMe,
            category = category,
            imageUrl = imageUrl
        )
    }
}
