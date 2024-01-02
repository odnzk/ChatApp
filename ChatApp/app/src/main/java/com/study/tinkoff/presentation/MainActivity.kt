package com.study.tinkoff.presentation

import android.os.Bundle
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.odnzk.auth.presentation.AuthStarter
import com.study.common.ext.fastLazy
import com.study.tinkoff.R
import com.study.tinkoff.databinding.ActivityMainBinding
import com.study.tinkoff.presentation.elm.MainActivityActor
import com.study.tinkoff.presentation.elm.MainActivityEffect
import com.study.tinkoff.presentation.elm.MainActivityEvent
import com.study.tinkoff.presentation.elm.MainActivityReducer
import com.study.tinkoff.presentation.elm.MainActivityState
import com.study.ui.NavConstants
import vivid.money.elmslie.android.base.ElmActivity
import vivid.money.elmslie.android.storeholder.LifecycleAwareStoreHolder
import vivid.money.elmslie.android.storeholder.StoreHolder
import vivid.money.elmslie.coroutines.ElmStoreCompat
import javax.inject.Inject
import com.study.channels.R as ChannelsR
import com.study.profile.R as ProfileR
import com.study.users.R as UsersR


class MainActivity : ElmActivity<MainActivityEvent, MainActivityEffect, MainActivityState>() {
    @Inject
    lateinit var actor: MainActivityActor

    @Inject
    lateinit var reducer: MainActivityReducer

    private lateinit var binding: ActivityMainBinding
    override val initEvent: MainActivityEvent = MainActivityEvent.Ui.Init

    override val storeHolder: StoreHolder<MainActivityEvent, MainActivityEffect, MainActivityState> by fastLazy {
        LifecycleAwareStoreHolder(lifecycle) {
            ElmStoreCompat(MainActivityState, reducer, actor)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        (applicationContext as ChatApp).appComponent.inject(this)
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initNavigation()
    }

    override fun onSupportNavigateUp() =
        findNavController(R.id.activity_main_fragment_container).navigateUp()

    override fun render(state: MainActivityState) = Unit

    override fun handleEffect(effect: MainActivityEffect) {
        when (effect) {
            MainActivityEffect.NavigateToLogin -> {
                AuthStarter.start(this)
            }

            is MainActivityEffect.Toast -> {
                Toast.makeText(this, effect.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun initNavigation() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.activity_main_fragment_container) as NavHostFragment
        val navController = navHostFragment.navController
        setupNavDestinationsListener(navController)
        binding.activityMainBottomNavigation.setupWithNavController(navController)
    }

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
