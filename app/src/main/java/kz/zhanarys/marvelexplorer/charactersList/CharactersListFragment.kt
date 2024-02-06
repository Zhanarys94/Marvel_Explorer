package kz.zhanarys.marvelexplorer.charactersList

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnScrollListener
import dagger.hilt.android.AndroidEntryPoint
import kz.zhanarys.domain.models.CharacterItemModel
import kz.zhanarys.marvelexplorer.CharactersListAdapter
import kz.zhanarys.marvelexplorer.SharedViewModel
import kz.zhanarys.marvelexplorer.databinding.FragmentCharactersBinding

@AndroidEntryPoint
class CharactersListFragment: Fragment() {
    private var binding: FragmentCharactersBinding? = null
    private var interactionListener: MainListFragmentInteractionListener? = null
    private val sharedViewModel: SharedViewModel by activityViewModels()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        interactionListener = context as MainListFragmentInteractionListener
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCharactersBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val searchBar = binding!!.listCharactersFragmentSearchBarEditText
        val savedListButton = binding!!.listCharactersFragmentButtonFavorites.apply {
            setOnClickListener {
                requestFocus()
                interactionListener!!.toSavedListButtonClick()
            }
        }
        val recyclerView = binding!!.listCharactersFragmentRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = CharactersListAdapter()
        recyclerView.addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))

        sharedViewModel.charactersListLiveData.observe(viewLifecycleOwner) { charactersList ->
            (recyclerView.adapter as CharactersListAdapter).submitList(charactersList.toList())
        }

        recyclerView.addOnScrollListener(object : OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                if (visibleItemCount + firstVisibleItemPosition >= totalItemCount
                    && firstVisibleItemPosition >= 0
                ) {
                    sharedViewModel.fetchMoreData()
                }
            }
        })

        (recyclerView.adapter as CharactersListAdapter).apply {
            setOnItemClickListener(
                object : CharactersListAdapter.OnItemClickListener {
                    override fun onItemClick(item: CharacterItemModel) {
                        // TODO
                    }
                }
            )

            setOnButtonLikeClickListener(
                object : CharactersListAdapter.OnButtonLikeClickListener {
                    override fun onLikeClick(item: CharacterItemModel) {
                        sharedViewModel.updateCharacterById(item.id)
                        if (item.isFavorite) {
                            sharedViewModel.removeFromFavorites(item.id)
                        } else {
                            sharedViewModel.addToFavorites(item.id)
                        }
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
                        sharedViewModel.searchForCharacterByNameStartingWith(searchText)
                    }

                    override fun afterTextChanged(p0: Editable?) {
                    }
                }
            )
        }

        savedListButton.setOnClickListener {
            interactionListener?.toSavedListButtonClick()
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

    interface MainListFragmentInteractionListener {
        fun onSearchBarChange(text: String)
        fun toSavedListButtonClick()
    }
}