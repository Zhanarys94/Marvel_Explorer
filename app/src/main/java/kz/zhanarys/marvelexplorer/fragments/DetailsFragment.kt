package kz.zhanarys.marvelexplorer.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.request.CachePolicy
import dagger.hilt.android.AndroidEntryPoint
import kz.zhanarys.marvelexplorer.viewModel.SharedViewModel
import kz.zhanarys.marvelexplorer.databinding.FragmentDetailsBinding
import kz.zhanarys.marvelexplorer.recyclerViewAdapters.ComicsListAdapter

@AndroidEntryPoint
class DetailsFragment: Fragment() {
    private var binding: FragmentDetailsBinding? = null
    private val sharedViewModel: SharedViewModel by activityViewModels()

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
                    sharedViewModel.fetchMoreComics()
                }
            }
        })

        sharedViewModel.currentCharacterLiveData.observe(viewLifecycleOwner) { character ->
            val imageUrl = character.imageUrl + imageVariant + character.imageExtension
            image.load(imageUrl) {
                crossfade(true)
                diskCachePolicy(CachePolicy.DISABLED)
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