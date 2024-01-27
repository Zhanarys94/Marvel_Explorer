package kz.zhanarys.marvelexplorer

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import kz.zhanarys.data.db.CharactersDatabase
import kz.zhanarys.marvelexplorer.databinding.ActivityMainBinding
import kz.zhanarys.marvelexplorer.heroesList.CharactersListFragment
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity(),
    CharactersListFragment.HeroesListFragmentInteractionListener
{
    private lateinit var binding: ActivityMainBinding
    @Inject lateinit var charactersDatabase: CharactersDatabase


    @Inject lateinit var viewModelFactory: SharedViewModelFactory
    private val sharedViewModel: SharedViewModel by viewModels {
        viewModelFactory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .setReorderingAllowed(true)
                .add(R.id.mainFragmentContainer, CharactersListFragment(), "MainHeroesListFragment")
                .commit()
        }
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedViewModel.fetchData()

/*        sharedViewModel.stateLiveData.observe(this) {
            if (it == ViewState.MAIN) {
                supportFragmentManager.beginTransaction()
                    .setReorderingAllowed(true)
                    .replace(R.id.mainFragmentContainer, HeroesListFragment(), "MainHeroesListFragment")
                    .commit()
            } else {
                supportFragmentManager.beginTransaction()
                    .setReorderingAllowed(true)
                    .replace(R.id.mainFragmentContainer, SavedHeroesListFragment(), "SavedListFragment")
                    .addToBackStack("SavedListFragment")
                    .commit()
                sharedViewModel.toSavedListButtonClick()
            }
        }*/
    }

    override fun onSearchBarChange(text: String) {
        sharedViewModel.setSearchBar(text)
    }

    override fun toSavedListButtonClick() {
/*        supportFragmentManager.beginTransaction()
            .setReorderingAllowed(true)
            .replace(R.id.mainFragmentContainer, SavedHeroesListFragment(), "SavedListFragment")
            .addToBackStack("SavedListFragment")
            .commit()
        sharedViewModel.toSavedListButtonClick()*/
    }
}