package kz.zhanarys.marvelexplorer
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import dagger.hilt.android.AndroidEntryPoint
import kz.zhanarys.data.repositories.local.CharactersLocalRepository
import kz.zhanarys.marvelexplorer.databinding.ActivityMainBinding
import kz.zhanarys.marvelexplorer.viewModel.SharedViewModel
import kz.zhanarys.marvelexplorer.viewModel.SharedViewModelFactory
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    @Inject lateinit var charactersLocalRepository: CharactersLocalRepository
    @Inject lateinit var viewModelFactory: SharedViewModelFactory
    private val sharedViewModel: SharedViewModel by viewModels {
        viewModelFactory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedViewModel.updateMainListData()

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.mainFragmentContainer) as NavHostFragment
        val navController = navHostFragment.navController
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        binding.toolbar.setupWithNavController(navController, appBarConfiguration)
    }
}