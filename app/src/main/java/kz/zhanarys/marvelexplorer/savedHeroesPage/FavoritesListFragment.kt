package kz.zhanarys.marvelexplorer.savedHeroesPage

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kz.zhanarys.domain.models.CharacterItemModel
import kz.zhanarys.marvelexplorer.SharedViewModel
import kz.zhanarys.marvelexplorer.databinding.FragmentFavoritesBinding
import kz.zhanarys.marvelexplorer.heroesList.HeroesListAdapter

@AndroidEntryPoint
class FavoritesListFragment: Fragment() {
    private var binding: FragmentFavoritesBinding? = null
    private var interactionListener: FavoritesListFragmentInteractionListener? = null
    private val sharedViewModel: SharedViewModel by activityViewModels()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        interactionListener = context as FavoritesListFragmentInteractionListener
    }
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
        val searchBar = binding!!.favoritesFragmentSearchBarEditText
        val recyclerView = binding!!.favoritesFragmentRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = HeroesListAdapter()

        sharedViewModel.favoritesListLiveData.observe(viewLifecycleOwner) { savedList ->
            (recyclerView.adapter as HeroesListAdapter).submitList(savedList.toList())
        }

        (recyclerView.adapter as HeroesListAdapter).apply {
            setOnItemClickListener(
                object : HeroesListAdapter.OnItemClickListener {
                    override fun onItemClick(item: CharacterItemModel, position: Int) {
                        // TODO
                    }
                }
            )

            setOnButtonLikeClickListener(
                object : HeroesListAdapter.OnButtonLikeClickListener {
                    override fun onLikeClick(item: CharacterItemModel) {
                        // TODO
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
/*                        val searchText = p0.toString()
                        if (searchText.length >= 3) {
                            sharedViewModel
                        }
                        interactionListener!!.onSearchBarChange(p0.toString())*/
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

    override fun onDetach() {
        super.onDetach()
        interactionListener = null
    }

    interface FavoritesListFragmentInteractionListener {
        fun onSearchBarChange(text: String)
    }
}