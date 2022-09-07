package ru.netology.masterchef.adapter


import android.widget.PopupMenu
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import ru.netology.masterchef.R
import ru.netology.masterchef.databinding.CardRecipeBinding
import ru.netology.masterchef.dto.Recipe


class RecipeViewHolder(
    private val binding: CardRecipeBinding,
    private val onInteractionListener: OnInteractionListener,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(recipe: Recipe) {
        binding.apply {
            favoriteImageButton.setOnClickListener {
                onInteractionListener.onLike(recipe)
            }
            menuImageButton.setOnClickListener {
                PopupMenu(it.context, it).apply {
                    inflate(R.menu.options_recipe)
                    setOnMenuItemClickListener { item ->
                        when (item.itemId) {
                            R.id.remove -> {
                                onInteractionListener.onRemoveById(recipe)
                                true
                            }
                            R.id.edit -> {
                                onInteractionListener.onEdit(recipe)
                                true
                            }
                            else -> false
                        }
                    }
                }.show()
            }
            textViewName.text = recipe.title
            textViewCategoryName.text = recipe.category
            favoriteImageButton.isChecked = recipe.favoriteByMe
            if (recipe.imageUrl != null) {
                Glide.with(itemView.context)
                    .load(recipe.imageUrl.toUri())
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.DATA)
                    .into(RecipeImageView)
            } else RecipeImageView.setImageResource(R.drawable.no_image_photo)
            root.setOnClickListener {
                onInteractionListener.onPostClicked(recipe)
            }
        }
    }
}