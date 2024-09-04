package com.example.budgetwise

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupWithNavController
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.example.budgetwise.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadNavigation()
    }

    private fun loadNavigation() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragment_container_view) as NavHostFragment
        val navController = navHostFragment.navController
        appBarConfiguration = AppBarConfiguration(
            topLevelDestinationIds = setOf(
                R.id.home_fragment,
                R.id.stats_fragment,
                R.id.account_fragment,
                R.id.more_fragment
            )
        )
        findViewById<Toolbar>(R.id.tb_toolbar)
            .setupWithNavController(navController, appBarConfiguration)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.home_fragment || destination.id == R.id.stats_fragment || destination.id == R.id.account_fragment || destination.id == R.id.more_fragment) {
                binding.tbToolbar.visibility = View.VISIBLE
                binding.bnNavigation.visibility = View.VISIBLE
            } else {
                binding.bnNavigation.visibility = View.GONE
            }
        }

        setupWithNavController(binding.bnNavigation, navController)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}