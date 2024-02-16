package kz.zhanarys.marvelexplorer
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import dagger.hilt.android.AndroidEntryPoint
import kz.zhanarys.data.repositories.local.CharactersLocalRepository
import kz.zhanarys.marvelexplorer.databinding.ActivityMainBinding
import kz.zhanarys.marvelexplorer.viewModel.SharedViewModelFactory
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var sharedPreferences: SharedPreferences
    @Inject lateinit var charactersLocalRepository: CharactersLocalRepository

    @Inject lateinit var viewModelFactory: SharedViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.MarvelExplorer)
        binding = ActivityMainBinding.inflate(layoutInflater)
        sharedPreferences = getPreferences(Context.MODE_PRIVATE)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.mainFragmentContainer) as NavHostFragment
        val navController = navHostFragment.navController
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        binding.toolbar.setupWithNavController(navController, appBarConfiguration)
    }

}