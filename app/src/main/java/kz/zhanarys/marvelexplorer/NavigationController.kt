package kz.zhanarys.marvelexplorer

import androidx.navigation.fragment.NavHostFragment
import kz.zhanarys.marvelexplorer.viewModel.SharedViewModel

class NavigationController(
    private val navHostFragment: NavHostFragment,
    private val viewModel: SharedViewModel
) {
    private val navController = navHostFragment.navController

    fun getNavController() = navController

    fun navigateToMainList() {
        navController.navigate("MainHeroesListFragment")
    }

    fun navigateToFavoritesList() {
        viewModel.goToFavoritesList()
        navController.navigate("FavoritesListFragment")
    }
}