package ru.netology.masterchef.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.masterchef.R
import ru.netology.masterchef.ui.NewRecipeFragment.Companion.idArg
import ru.netology.masterchef.adapter.OnInteractionListener
import ru.netology.masterchef.adapter.RecipeAdapter
import ru.netology.masterchef.databinding.FragmentFavoritesBinding
import ru.netology.masterchef.dto.Recipe
import ru.netology.masterchef.viewModel.RecipeViewModel


class FavoritesFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        val viewModel: RecipeViewModel by viewModels(ownerProducer = ::requireParentFragment)
        val adapter = RecipeAdapter(object : OnInteractionListener {

            override fun onEdit(recipe: Recipe) {
                viewModel.edit(recipe)
                findNavController().navigate(R.id.newRecipeFragment,
                    Bundle().apply { idArg = recipe.id })
            }

            override fun onLike(recipe: Recipe) {
                viewModel.like(recipe.id)
            }

            override fun onRemoveById(recipe: Recipe) {
                viewModel.removeByID(recipe.id)
            }

            override fun onPostClicked(recipe: Recipe) {
                findNavController().navigate(R.id.recipeFragment,
                    Bundle().apply { idArg = recipe.id })
            }
        })

        with(binding) {
            listFavorites.adapter = adapter
            viewModel.selectFavorites().observe(viewLifecycleOwner) { list ->
                if (list.isEmpty()) {
                    listFavorites.visibility = View.GONE
                    emptyList.visibility = View.VISIBLE
                    emptyTextView.visibility = View.VISIBLE
                } else {
                    adapter.submitList(list)
                    listFavorites.visibility = View.VISIBLE
                    emptyList.visibility = View.GONE
                    emptyTextView.visibility = View.GONE
                }
            }
        }
        return binding.root
    }
}