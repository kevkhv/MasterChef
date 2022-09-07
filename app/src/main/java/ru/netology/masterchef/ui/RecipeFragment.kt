package ru.netology.masterchef.ui


import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ScrollView
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import ru.netology.masterchef.R
import ru.netology.masterchef.databinding.FragmentRecipeBinding
import ru.netology.masterchef.utils.IntArg
import ru.netology.masterchef.viewModel.RecipeViewModel


class RecipeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val binding = FragmentRecipeBinding.inflate(inflater, container, false)
        val viewModel: RecipeViewModel by viewModels(ownerProducer = ::requireParentFragment)

        with(binding) {
            viewModel.data.value?.firstOrNull { it.id == arguments?.idArg }?.let { recipe ->
                titleRecipeView.text = recipe.title
                categoryView.text = "${getString(R.string.category)} ${recipe.category}"
                favoriteImageButton.isChecked = recipe.favoriteByMe
                favoriteImageButton.setOnClickListener {
                    viewModel.like(recipe.id)
                }
                contentView.text = recipe.content
                val components = getString(R.string.ingredients) +
                        getString(R.string.apple) +
                        getString(R.string.sugar) +
                        getString(R.string.garlic) +
                        getString(R.string.apple_vinegar) +
                        getString(R.string.walnut)
                val spanned = Html.fromHtml(components)
                materials.text = spanned
                if (recipe.imageUrl != null) {
                    Glide
                        .with(this@RecipeFragment)
                        .load(recipe.imageUrl.toUri())
                        .centerCrop()
                        .diskCacheStrategy(DiskCacheStrategy.DATA)
                        .into(recipeImageView)
                } else return@let
            }

            startButton.setOnClickListener {
                startButton.text = getString(R.string.bon_appetit)
                startButton.isEnabled = false
                contentView.visibility = View.VISIBLE
                scrollView.postDelayed({ scrollView.fullScroll(ScrollView.FOCUS_DOWN) }, 100)
            }

            closeButton.setOnClickListener {
                findNavController().navigateUp()
            }
        }
        return binding.root
    }

    companion object {
        var Bundle.idArg: Int? by IntArg
    }
}