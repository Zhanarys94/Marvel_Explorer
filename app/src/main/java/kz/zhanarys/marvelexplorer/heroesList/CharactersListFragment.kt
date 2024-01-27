package kz.zhanarys.marvelexplorer.heroesList

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
import kz.zhanarys.domain.entities.CharacterEntity
import kz.zhanarys.marvelexplorer.SharedViewModel
import kz.zhanarys.marvelexplorer.databinding.ListFragmentHeroesBinding
import kotlin.math.max

@AndroidEntryPoint
class CharactersListFragment: Fragment() {
    private var binding: ListFragmentHeroesBinding? = null
    private var interactionListener: HeroesListFragmentInteractionListener? = null
    private val sharedViewModel: SharedViewModel by activityViewModels()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        interactionListener = context as HeroesListFragmentInteractionListener
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ListFragmentHeroesBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val searchBar = binding!!.listHeroesFragmentSearchBarEditText
        val savedListButton = binding!!.listHeroesFragmentToSavedListButton.apply {
            setOnClickListener {
                requestFocus()
                interactionListener!!.toSavedListButtonClick()
            }
        }
        val recyclerView = binding!!.listHeroesFragmentRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = HeroesListAdapter()
        recyclerView.addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
        val attrs = intArrayOf(android.R.attr.listDivider)
        val styledAttributes = requireContext().obtainStyledAttributes(attrs)
        val divider = styledAttributes.getDrawable(0)
        val dividerHeight = divider!!.intrinsicHeight
        styledAttributes.recycle()

        sharedViewModel.heroesListLiveData.observe(viewLifecycleOwner) { _heroesList ->
            val heroesList = _heroesList.map { CharacterEntity(it.id, it.name, it.imageUrl, it.imageExtension, it.shortInfo ) }
            (recyclerView.adapter as HeroesListAdapter).submitList(heroesList.toList())
        }

        recyclerView.addOnScrollListener(object : OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
                val correctedFirstVisibleItemPosition = max(0, firstVisibleItemPosition - dividerHeight)
                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                if (visibleItemCount + firstVisibleItemPosition >= totalItemCount
                    && firstVisibleItemPosition >= 0
                ) {
                    sharedViewModel.fetchMoreData()
                }
            }
        })

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
            addTextChangedListener(
                object : TextWatcher {
                    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    }

                    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
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

    interface HeroesListFragmentInteractionListener {
        fun onSearchBarChange(text: String)
        fun toSavedListButtonClick()
    }
}