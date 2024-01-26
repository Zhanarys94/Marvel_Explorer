package kz.zhanarys.marvelexplorer

import androidx.appcompat.app.AppCompatActivity
import androidx.activity.viewModels
import android.os.Bundle
import kz.zhanarys.data.HeroesDatabase
import kz.zhanarys.domain.model.viewState.ViewState
import kz.zhanarys.marvelexplorer.databinding.ActivityMainBinding
import kz.zhanarys.marvelexplorer.heroesList.HeroesListFragment
import kz.zhanarys.marvelexplorer.savedHeroesPage.SavedHeroesListFragment

class MainActivity : AppCompatActivity(),
    HeroesListFragment.HeroesListFragmentInteractionListener
{
    private lateinit var binding: ActivityMainBinding
    private lateinit var heroesDatabase: HeroesDatabase
    private val viewModelFactory: SharedViewModelFactory by lazy {
        val application = (application as MarvelExplorerApp)
        heroesDatabase = application.heroesDatabase
        SharedViewModelFactory(heroesDatabase)
    }
    private val sharedViewModel: SharedViewModel by viewModels {
        viewModelFactory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .setReorderingAllowed(true)
                .add(R.id.mainFragmentContainer, HeroesListFragment(), "MainHeroesListFragment")
                .commit()
        }
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedViewModel.stateLiveData.observe(this) {
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
        }
    }

    override fun onSearchBarChange(text: String) {
        sharedViewModel.setSearchBar(text)
    }

    override fun toSavedListButtonClick() {
        supportFragmentManager.beginTransaction()
            .setReorderingAllowed(true)
            .replace(R.id.mainFragmentContainer, SavedHeroesListFragment(), "SavedListFragment")
            .addToBackStack("SavedListFragment")
            .commit()
        sharedViewModel.toSavedListButtonClick()
    }
}