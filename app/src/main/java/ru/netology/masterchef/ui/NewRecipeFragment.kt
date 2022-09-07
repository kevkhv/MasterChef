package ru.netology.masterchef.ui


import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.addCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import ru.netology.masterchef.R
import ru.netology.masterchef.databinding.FragmentNewRecipeBinding
import ru.netology.masterchef.utils.IntArg
import ru.netology.masterchef.viewModel.RecipeViewModel
import ru.netology.masterchef.utils.bundle


class NewRecipeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val binding = FragmentNewRecipeBinding.inflate(inflater, container, false)

        val viewModel: RecipeViewModel by viewModels(ownerProducer = ::requireParentFragment)

        val getImage: ActivityResultLauncher<String> =
            registerForActivityResult(ActivityResultContracts.GetContent()) { imageUri: Uri? ->
                imageUri ?: return@registerForActivityResult
                Glide.with(this)
                    .load(imageUri)
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.DATA)
                    .into(binding.imageView)
                bundle.putString(URI_KEY, imageUri.toString())
            }

        val currentRecipe = viewModel.data.value?.firstOrNull { it.id == arguments?.idArg }
        with(binding) {
            val items = resources.getStringArray(R.array.category_items)
            val adapter = ArrayAdapter(requireContext(), R.layout.list_item, items)
            (menu.editText as? AutoCompleteTextView)?.setAdapter(adapter)

            currentRecipe?.let { recipe ->
                authorTextView.setText(recipe.author)
                titleView.setText(recipe.title)
                stepTextView.setText(recipe.content)
                if (!bundle.getString(URI_KEY).isNullOrBlank()) {
                    Glide
                        .with(this@NewRecipeFragment)
                        .load(bundle.getString(URI_KEY)?.toUri())
                        .centerCrop()
                        .diskCacheStrategy(DiskCacheStrategy.DATA)
                        .into(imageView)
                } else if (!recipe.imageUrl.isNullOrBlank()) {
                    Glide
                        .with(this@NewRecipeFragment)
                        .load(recipe.imageUrl.toUri())
                        .centerCrop()
                        .diskCacheStrategy(DiskCacheStrategy.DATA)
                        .into(imageView)
                } else {
                    imageView.setImageResource(R.drawable.no_image_photo)
                }
            } ?: if (!bundle.getString(URI_KEY).isNullOrBlank()) {
                Glide
                    .with(this@NewRecipeFragment)
                    .load(bundle.getString(URI_KEY)?.toUri())
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.DATA)
                    .into(imageView)
            } else imageView.setImageResource(R.drawable.no_image_photo)

            uploadButton.setOnClickListener {
                getImage.launch("image/*")
            }

            saveButton.setOnClickListener {
                if (!stepTextView.text.isNullOrBlank() &&
                    !authorTextView.text.isNullOrBlank() &&
                    !menu.editText?.text.isNullOrBlank() &&
                    !titleView.text.isNullOrBlank()
                ) {
                    val content = stepTextView.text.toString()
                    val author = authorTextView.text.toString()
                    val title = titleView.text.toString()
                    val category = menu.editText?.text.toString()
                    val imageUrl = if (bundle.getString(URI_KEY) == null) currentRecipe?.imageUrl
                    else bundle.getString(URI_KEY)
                    viewModel.changeContent(
                        content,
                        author,
                        title,
                        category,
                        imageUrl
                    )
                    viewModel.save()
                    bundle.putString(URI_KEY, null)
                    findNavController().navigateUp()
                } else {
                    Toast.makeText(activity,
                        getString(R.string.toast_empty_fields),
                        Toast.LENGTH_SHORT)
                        .show()
                }
            }

            requireActivity().onBackPressedDispatcher.addCallback(this@NewRecipeFragment) {
                bundle.putString(URI_KEY, null)
                viewModel.editedReset()
                findNavController().navigateUp()
            }
        }
        return binding.root
    }


    companion object {
        var Bundle.idArg: Int? by IntArg
        const val URI_KEY = "URI"
    }
}



