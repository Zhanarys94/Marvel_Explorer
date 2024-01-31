package kz.zhanarys.marvelexplorer

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import kz.zhanarys.data.repositories.local.CharactersLocalDatabase
import kz.zhanarys.marvelexplorer.databinding.ActivityMainBinding
import kz.zhanarys.marvelexplorer.heroesList.CharactersListFragment
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity(),
    CharactersListFragment.MainListFragmentInteractionListener
{
    private lateinit var binding: ActivityMainBinding
    @Inject lateinit var charactersLocalDatabase: CharactersLocalDatabase


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

        setSupportActionBar(binding.toolbar)
        val actionBar = supportActionBar
        actionBar!!.setDisplayHomeAsUpEnabled(true)
        actionBar.setDisplayShowHomeEnabled(true)

        sharedViewModel.updateMainListData()

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

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
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