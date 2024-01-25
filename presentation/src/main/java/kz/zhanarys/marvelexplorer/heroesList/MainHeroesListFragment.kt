package kz.zhanarys.marvelexplorer.heroesList

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import kz.zhanarys.domain.HeroShortInfo
import kz.zhanarys.marvelexplorer.databinding.ListFragmentHeroesBinding

class MainHeroesListFragment: Fragment() {
    private var binding: ListFragmentHeroesBinding? = null
    private var interactionListener: HeroesListFragmentInteractionListener? = null

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

        (recyclerView.adapter as HeroesListAdapter).apply {
            setOnItemClickListener(
                object : HeroesListAdapter.OnItemClickListener {
                    override fun onItemClick(item: HeroShortInfo, position: Int) {
                        // TODO
                    }
                }
            )

            setOnButtonLikeClickListener(
                object : HeroesListAdapter.OnButtonLikeClickListener {
                    override fun onLikeClick(item: HeroShortInfo) {
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