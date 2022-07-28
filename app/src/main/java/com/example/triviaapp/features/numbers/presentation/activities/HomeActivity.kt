package com.example.triviaapp.features.numbers.presentation.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.core.view.GravityCompat
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.*
import com.example.triviaapp.R
import com.example.triviaapp.databinding.ActivityHomeBinding
import com.example.triviaapp.features.numbers.presentation.fragments.NumbersFragment
import com.example.triviaapp.features.numbers.presentation.viewmodels.NumbersViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {

    lateinit var binding: ActivityHomeBinding
    lateinit var appBarConfig: AppBarConfiguration
    lateinit var navController: NavController
    private val veiwmodel: NumbersViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navController = findNavController(R.id.nav_host_fragment)

        binding.bottomNavView.setupWithNavController(navController)

        appBarConfig = AppBarConfiguration(setOf(R.id.numbersFragment, R.id.quizFragment),
            binding.homeDrawerLayout)
        setSupportActionBar(binding.toolBar)

        binding.homeNavView.setupWithNavController(navController)
        setupActionBarWithNavController(navController, appBarConfig)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return item.onNavDestinationSelected(navController) || super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
        return findNavController(R.id.nav_host_fragment).navigateUp(appBarConfig)
    }

    override fun onBackPressed() {
        if (binding.homeDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.homeDrawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
}