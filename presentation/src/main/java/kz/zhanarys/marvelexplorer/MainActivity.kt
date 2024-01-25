package kz.zhanarys.marvelexplorer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kz.zhanarys.marvelexplorer.databinding.ActivityMainBinding
import kz.zhanarys.marvelexplorer.heroesList.MainHeroesListFragment

class MainActivity : AppCompatActivity(),
    MainHeroesListFragment.HeroesListFragmentInteractionListener
{
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .setReorderingAllowed(true)
                .add(R.id.mainFragmentContainer, MainHeroesListFragment(), "MainHeroesListFragment")
                .commit()
        }
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onSearchBarChange(text: String) {
        TODO("Not yet implemented")
    }

    override fun toSavedListButtonClick() {
        TODO("Not yet implemented")
    }
}