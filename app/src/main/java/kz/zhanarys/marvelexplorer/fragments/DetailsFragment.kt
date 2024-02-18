package kz.zhanarys.marvelexplorer.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kz.zhanarys.domain.interfaces.repositories.local.ImageHandler
import kz.zhanarys.marvelexplorer.R
import kz.zhanarys.marvelexplorer.viewModel.SharedViewModel
import kz.zhanarys.marvelexplorer.databinding.FragmentDetailsBinding
import kz.zhanarys.marvelexplorer.recyclerViewAdapters.ComicsListAdapter
import javax.inject.Inject

@AndroidEntryPoint
class DetailsFragment: Fragment() {
    private var binding: FragmentDetailsBinding? = null
    private val sharedViewModel: SharedViewModel by activityViewModels()
    @Inject lateinit var imageHandler: ImageHandler

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val image = binding!!.characterDetailsImageView
        val imageVariant = ""
        val name = binding!!.characterDetailsNameTextView
        val description = binding!!.characterDetailsDescriptionTextView
        val internetConnectionTextView = binding!!.characterDetailsNoInternetTextView

        sharedViewModel.internetConnection.observe(viewLifecycleOwner) {
            if (it) {
                internetConnectionTextView.visibility = View.GONE
            } else {
                internetConnectionTextView.visibility = View.VISIBLE
            }
        }
        val recyclerView = binding!!.characterDetailsRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = ComicsListAdapter()

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                if (visibleItemCount + firstVisibleItemPosition >= totalItemCount
                    && firstVisibleItemPosition >= 0
                ) {
                    if (sharedViewModel.internetConnection.value == true) {
                        sharedViewModel.fetchMoreComics()
                    }
                }
            }
        })

        sharedViewModel.currentCharacterLiveData.observe(viewLifecycleOwner) { character ->
            lifecycleScope.launch {
                val imageUrl = character.imageUrl + imageVariant + character.imageExtension
                val bitmap = imageHandler.getImage(character.id.toString())
                if (bitmap != null) {
                    image.setImageBitmap(bitmap)
                } else if (sharedViewModel.internetConnection.value == true) {
                    image.load(imageUrl) {
                        crossfade(true)
                        error(R.drawable.ic_loading_placeholder)
                    }
                }
            }
            name.text = character.name
            description.text = character.shortInfo.ifEmpty { "No information about this character" }
        }
        sharedViewModel.comicsListLiveData.observe(viewLifecycleOwner) { comics ->
            (recyclerView.adapter as ComicsListAdapter).submitList(comics.toList())
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

}