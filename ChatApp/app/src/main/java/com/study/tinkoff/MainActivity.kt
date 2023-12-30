package com.study.tinkoff

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.study.tinkoff.databinding.ActivityMainBinding
import com.study.ui.NavConstants
import com.study.channels.R as ChannelsR
import com.study.profile.R as ProfileR
import com.study.users.R as UsersR


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        (applicationContext as ChatApp).appComponent.inject(this)
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.activity_main_fragment_container) as NavHostFragment
        val navController = navHostFragment.navController
        setupNavDestinationsListener(navController)
        binding.activityMainBottomNavigation.setupWithNavController(navController)
    }

    override fun onSupportNavigateUp() =
        findNavController(R.id.activity_main_fragment_container).navigateUp()

    private fun setupNavDestinationsListener(navController: NavController) {
        navController.addOnDestinationChangedListener { _, destination, arguments ->
            val isBottomNavVisible = when (destination.id) {
                ProfileR.id.profileFragment ->
                    arguments?.getInt(NavConstants.USER_ID_KEY) == NavConstants.CURRENT_USER_ID_KEY

                ChannelsR.id.holderChannelsFragment, UsersR.id.usersFragment -> true
                else -> false
            }
            binding.activityMainBottomNavigation.isVisible = isBottomNavVisible
        }
    }

}
