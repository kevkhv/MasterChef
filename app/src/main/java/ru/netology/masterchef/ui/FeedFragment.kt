package ru.netology.masterchef.ui


import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.core.view.iterator
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import com.google.android.material.chip.Chip
import ru.netology.masterchef.R
import ru.netology.masterchef.ui.NewRecipeFragment.Companion.idArg
import ru.netology.masterchef.adapter.OnInteractionListener
import ru.netology.masterchef.adapter.RecipeAdapter
import ru.netology.masterchef.databinding.FragmentFeedBinding
import ru.netology.masterchef.dto.Recipe
import ru.netology.masterchef.viewModel.RecipeViewModel


class FeedFragment : Fragment(), SearchView.OnQueryTextListener {

    lateinit var binding: FragmentFeedBinding
    val viewModel: RecipeViewModel by viewModels(ownerProducer = ::requireParentFragment)
    private val adapter = RecipeAdapter(object : OnInteractionListener {

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


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentFeedBinding.inflate(layoutInflater)
        with(binding) {
            listView.adapter = adapter
            chipGroup.checkedChipIds.forEach { id ->
                val chip = chipGroup.findViewById<Chip>(id)
                chip.setOnCheckedChangeListener { view, _ ->
                    viewModel.selectCategory(view.isChecked, view.text.toString())
                }
            }
        }

        viewModel.categoryList.observe(viewLifecycleOwner) { list ->
            viewModel.searchDatabase(category = list)
            println(viewModel.categoryList.value.toString())
        }

        viewModel.filteredList.observe(viewLifecycleOwner) { list ->
            with(binding) {
                if (list.isEmpty()) {
                    listView.visibility = View.GONE
                    notFoundConstraint.visibility = View.VISIBLE

                } else {
                    adapter.submitList(list)
                    listView.visibility = View.VISIBLE
                    notFoundConstraint.visibility = View.GONE
                }
            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {

            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                with(binding) {
                    menuInflater.inflate(R.menu.top_app_bar, menu)
                    val filters = menu.findItem(R.id.action_filters)
                    val search = menu.findItem(R.id.action_search)
                    val searchView: SearchView = search.actionView as SearchView
                    searchView.queryHint = getString(R.string.find_recipes)
                    searchView.clearFocus()
                    searchView.setOnQueryTextListener(this@FeedFragment)

                    search.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
                        override fun onMenuItemActionExpand(menuItem: MenuItem?): Boolean {
                            filters.isVisible = true
                            return true
                        }

                        override fun onMenuItemActionCollapse(menuItem: MenuItem?): Boolean {
                            filters.isVisible = false
                            filters.isChecked = false
                            chipGroup.visibility = View.GONE
                            for (item in chipGroup) {
                                chipGroup.check(item.id)
                            }
                            return true
                        }
                    })

                    filters.setOnMenuItemClickListener {
                        if (!it.isChecked) chipGroup.visibility = View.VISIBLE
                        else chipGroup.visibility = View.GONE
                        it.isChecked = !it.isChecked
                        true
                    }

                }
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.action_search -> true
                    R.id.action_filters -> true
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        viewModel.searchDatabase("%$query%")
        return true
    }

    override fun onQueryTextChange(query: String?): Boolean {
        viewModel.searchDatabase("%$query%")
        return true
    }
}
