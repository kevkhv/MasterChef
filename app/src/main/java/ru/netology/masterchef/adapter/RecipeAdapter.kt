package ru.netology.masterchef.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import ru.netology.masterchef.databinding.CardRecipeBinding
import ru.netology.masterchef.dto.Recipe

interface OnInteractionListener {
    fun onLike(recipe: Recipe){}
    fun onEdit(recipe: Recipe){}
    fun onRemoveById(recipe: Recipe)
    fun onPostClicked(recipe: Recipe)
}

class RecipeAdapter(private val onInteractionListener:OnInteractionListener) :
    ListAdapter<Recipe, RecipeViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        val binding = CardRecipeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RecipeViewHolder(binding,onInteractionListener)
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    private object DiffCallback : DiffUtil.ItemCallback<Recipe>() {

        override fun areItemsTheSame(oldItem: Recipe, newItem: Recipe) =
            oldItem.id == newItem.id

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: Recipe, newItem: Recipe) =
            oldItem == newItem
    }
}