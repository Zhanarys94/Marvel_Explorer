package kz.zhanarys.marvelexplorer.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnScrollListener
import dagger.hilt.android.AndroidEntryPoint
import kz.zhanarys.domain.interfaces.repositories.local.ImageHandler
import kz.zhanarys.domain.models.CharacterItemModel
import kz.zhanarys.marvelexplorer.recyclerViewAdapters.CharactersListAdapter
import kz.zhanarys.marvelexplorer.R
import kz.zhanarys.marvelexplorer.viewModel.SharedViewModel
import kz.zhanarys.marvelexplorer.databinding.FragmentCharactersBinding
import javax.inject.Inject

@AndroidEntryPoint
class CharactersListFragment: Fragment() {
    private var binding: FragmentCharactersBinding? = null
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private lateinit var navController: NavController
    @Inject lateinit var imageHandler: ImageHandler

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

        navController = findNavController()
        val progressBar = binding!!.listCharactersFragmentProgressBar

        sharedViewModel.isLoading.observe(viewLifecycleOwner) {
            progressBar.visibility = if (it) View.VISIBLE else View.GONE
        }

        sharedViewModel.internetConnection.observe(viewLifecycleOwner) {
            if (!it) Toast.makeText(context, "No internet connection", Toast.LENGTH_SHORT).show()
        }

        val searchBar = binding!!.listCharactersFragmentSearchBarEditText.apply {
            setSelection(text.length)
            addTextChangedListener(
                object : TextWatcher {
                    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                        sharedViewModel.checkInternetConnection()
                    }

                    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                        val searchText = p0.toString()
                        if (sharedViewModel.internetConnection.value == true && searchText.isNotEmpty()) {
                            sharedViewModel.searchForCharacterByNameStartingWith(searchText)
                        }
                    }

                    override fun afterTextChanged(p0: Editable?) {
                    }
                }
            )
        }

        val favoritesListButton = binding!!.listCharactersFragmentButtonFavorites.apply {
            setOnClickListener {
                navController.navigate(R.id.action_charactersFragment_to_favoritesListFragment)
                sharedViewModel.goToFavoritesList()
            }
        }

        val swipeRefreshLayout = binding!!.listCharactersFragmentSwipeRefreshLayout.apply {
            setOnRefreshListener {
                sharedViewModel.checkInternetConnection()
                if (sharedViewModel.internetConnection.value == true) {
                    sharedViewModel.updateMainListData()
                    searchBar.text = null
                    isRefreshing = false
                }
            }
        }

        val recyclerView = binding!!.listCharactersFragmentRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = CharactersListAdapter(imageHandler, lifecycleScope)
        configureRecyclerView(recyclerView)

        sharedViewModel.charactersListLiveData.observe(viewLifecycleOwner) { charactersList ->
            (recyclerView.adapter as CharactersListAdapter).submitList(charactersList.toList())
        }

    }

    private fun configureRecyclerView(recyclerView: RecyclerView) {
        recyclerView.addOnScrollListener(object : OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                sharedViewModel.checkInternetConnection()
                if (binding!!.listCharactersFragmentSearchBarEditText.text.isEmpty()) {
                    val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                    val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
                    val visibleItemCount = layoutManager.childCount
                    val totalItemCount = layoutManager.itemCount
                    if (visibleItemCount + firstVisibleItemPosition >= totalItemCount
                        && firstVisibleItemPosition >= 0
                    ) {
                        if (sharedViewModel.internetConnection.value == true) {
                            sharedViewModel.fetchMoreData()
                        }
                    }
                } else {
                    val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                    val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
                    val visibleItemCount = layoutManager.childCount
                    val totalItemCount = layoutManager.itemCount
                    if (visibleItemCount + firstVisibleItemPosition >= totalItemCount
                        && firstVisibleItemPosition >= 0
                    ) {
                        if (sharedViewModel.internetConnection.value == true) {
                            sharedViewModel.fetchMoreData(binding!!.listCharactersFragmentSearchBarEditText.text.toString())
                        }
                    }
                }
            }
        })

        (recyclerView.adapter as CharactersListAdapter).apply {
            setOnItemClickListener(
                object : CharactersListAdapter.OnItemClickListener {
                    override fun onItemClick(item: CharacterItemModel) {
                        sharedViewModel.checkInternetConnection()
                        if (sharedViewModel.internetConnection.value == true) {
                            sharedViewModel.getCharacterDetails(item)
                            navController.navigate(R.id.action_charactersFragment_to_detailsFragment)
                        } else {
                            Toast.makeText(context, "No internet connection", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            )

            setOnButtonLikeClickListener(
                object : CharactersListAdapter.OnButtonLikeClickListener {
                    override fun onLikeClick(item: CharacterItemModel) {
                        sharedViewModel.checkInternetConnection()
                        if (sharedViewModel.internetConnection.value == true) {
                            sharedViewModel.updateCharacterById(item.id)
                            if (item.isFavorite) {
                                sharedViewModel.removeFromFavorites(item.id)
                            } else {
                                sharedViewModel.addToFavorites(item.id)
                            }
                        } else if (item.isFavorite) {
                            sharedViewModel.updateCharacterById(item.id)
                            sharedViewModel.removeFromFavorites(item.id)
                        }
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