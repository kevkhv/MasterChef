package ru.netology.masterchef.viewModel

import android.app.Application
import androidx.lifecycle.*
import ru.netology.masterchef.R
import ru.netology.masterchef.dto.Recipe
import ru.netology.masterchef.repository.RecipeRepository
import ru.netology.masterchef.repository.RecipeRepositoryImpl
import ru.netology.nmedia.db.AppDb

private val empty = Recipe(
    id = 0,
    author = "",
    title = "",
    content = "",
    favoriteByMe = false,
    category = "",
    imageUrl = null
)

class RecipeViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: RecipeRepository =
        RecipeRepositoryImpl(dao = AppDb.getInstance(context = application).recipeDao)

    val data = repository.get()

    val edited = MutableLiveData(empty)

    var filteredList = Transformations.distinctUntilChanged(data) as MutableLiveData<List<Recipe>>

    val categoryList =
        MutableLiveData(mutableListOf(*application.resources.getStringArray(R.array.category_items)))

    fun selectFavorites() = repository.selectFavorites(true)

    fun like(recipeId: Int) = repository.like(recipeId)

    fun save() {
        edited.value?.let {
            repository.save(it)
        }
        edited.value = empty
    }

    fun changeContent(
        content: String,
        author: String,
        title: String,
        category: String,
        imageUrl: String?,
    ) {
        edited.value?.let {
            val content = content.trim()
            val author = author.trim()
            val category = category.trim()
            val title = title.trim()
            val imageUrl = imageUrl?.trim()
            if (it.content == content && it.author == author && it.category == category && it.title == title && it.imageUrl == imageUrl) {
                return
            }
            edited.value = it.copy(
                content = content,
                author = author,
                category = category,
                title = title,
                imageUrl = imageUrl
            )
        }
    }

    fun removeByID(recipeId: Int) = repository.removeByID(recipeId)

    fun edit(recipe: Recipe) {
        edited.value = recipe
    }

    fun selectCategory(flag: Boolean, itemCategory: String) {
        categoryList.value =
            if (flag) categoryList.value?.plus(itemCategory)?.toMutableList()
            else categoryList.value?.minus(itemCategory)?.toMutableList()
    }

    fun searchDatabase(
        searchQuery: String? = "%%",
        category: MutableList<String>? = categoryList.value,
    ) {
        filteredList.value = repository.searchDatabase(searchQuery, category)
    }

    fun editedReset() {
        edited.value = empty
    }
}




