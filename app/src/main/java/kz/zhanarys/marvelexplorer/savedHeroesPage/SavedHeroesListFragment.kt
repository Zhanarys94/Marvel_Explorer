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
import kz.zhanarys.domain.entities.CharacterEntity
import kz.zhanarys.marvelexplorer.SharedViewModel
import kz.zhanarys.marvelexplorer.databinding.ListFragmentSavedBinding
import kz.zhanarys.marvelexplorer.heroesList.HeroesListAdapter

@AndroidEntryPoint
class SavedHeroesListFragment: Fragment() {
    private var binding: ListFragmentSavedBinding? = null
    private var interactionListener: SavedListFragmentInteractionListener? = null
    private val sharedViewModel: SharedViewModel by activityViewModels()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        interactionListener = context as SavedListFragmentInteractionListener
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ListFragmentSavedBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val searchBar = binding!!.listHeroesFragmentSearchBarEditText
        val recyclerView = binding!!.listHeroesFragmentRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = HeroesListAdapter()

        sharedViewModel.savedListLiveData.observe(viewLifecycleOwner) { savedList ->
            val heroesList = savedList.map { CharacterEntity(it.id, it.name, it.imageUrl, it.shortInfo, it.imageExtension ) }
            (recyclerView.adapter as HeroesListAdapter).submitList(heroesList.toList())
        }

        (recyclerView.adapter as HeroesListAdapter).apply {
            setOnItemClickListener(
                object : HeroesListAdapter.OnItemClickListener {
                    override fun onItemClick(item: CharacterEntity, position: Int) {
                        // TODO
                    }
                }
            )

            setOnButtonLikeClickListener(
                object : HeroesListAdapter.OnButtonLikeClickListener {
                    override fun onLikeClick(item: CharacterEntity) {
                        // TODO
                    }
                }
            )
        }

        searchBar.apply {
            requestFocus()
            setSelection(text.length)

            addTextChangedListener(
                object : TextWatcher {
                    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    }

                    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                        interactionListener!!.onSearchBarChange(p0.toString())
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

    interface SavedListFragmentInteractionListener {
        fun onSearchBarChange(text: String)
    }
}