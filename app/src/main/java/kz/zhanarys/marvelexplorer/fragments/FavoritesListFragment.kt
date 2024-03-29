package kz.zhanarys.marvelexplorer.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kz.zhanarys.domain.interfaces.repositories.local.ImageHandler
import kz.zhanarys.domain.models.CharacterItemModel
import kz.zhanarys.marvelexplorer.R
import kz.zhanarys.marvelexplorer.viewModel.SharedViewModel
import kz.zhanarys.marvelexplorer.databinding.FragmentFavoritesBinding
import kz.zhanarys.marvelexplorer.recyclerViewAdapters.CharactersListAdapter
import javax.inject.Inject

@AndroidEntryPoint
class FavoritesListFragment: Fragment() {
    private var binding: FragmentFavoritesBinding? = null
    private val sharedViewModel: SharedViewModel by activityViewModels()
    @Inject lateinit var imageHandler: ImageHandler

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val navController = findNavController()
        val searchBar = binding!!.favoritesFragmentSearchBarEditText
        val recyclerView = binding!!.favoritesFragmentRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = CharactersListAdapter(imageHandler, lifecycleScope)

        sharedViewModel.favoritesListLiveData.observe(viewLifecycleOwner) { _savedList ->
            val savedList = _savedList.map {
                it.isFavorite = true
                it
            }
            (recyclerView.adapter as CharactersListAdapter).submitList(savedList)
        }

        (recyclerView.adapter as CharactersListAdapter).apply {
            setOnItemClickListener(
                object : CharactersListAdapter.OnItemClickListener {
                    override fun onItemClick(item: CharacterItemModel) {
                        sharedViewModel.getCharacterDetails(item)
                        navController.navigate(R.id.action_favoritesFragment_to_detailsFragment)
                    }
                }
            )

            setOnButtonLikeClickListener(
                object : CharactersListAdapter.OnButtonLikeClickListener {
                    override fun onLikeClick(item: CharacterItemModel) {
                        sharedViewModel.removeFromFavorites(item.id)
                    }
                }
            )
        }

        searchBar.apply {
            setSelection(text.length)

            addTextChangedListener(
                object : TextWatcher {
                    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    }

                    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                        val searchText = p0.toString()
                        if (searchText.isNotEmpty()) {
                            sharedViewModel.searchForCharacterByNameStartingWithInDb(searchText)
                        }
                    }

                    override fun afterTextChanged(p0: Editable?) {
                    }
                }
            )
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

}