package com.study.tinkoff


import android.os.Bundle
import android.view.Menu
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.study.common.extension.fastLazy
import com.study.components.extension.createStoreHolder
import com.study.tinkoff.databinding.ActivityMainBinding
import com.study.tinkoff.elm.MainActor
import com.study.tinkoff.elm.MainEffect
import com.study.tinkoff.elm.MainEvent
import com.study.tinkoff.elm.MainState
import com.study.ui.NavConstants
import vivid.money.elmslie.android.base.ElmActivity
import vivid.money.elmslie.android.storeholder.StoreHolder
import vivid.money.elmslie.core.store.Store
import javax.inject.Inject
import com.study.channels.R as ChannelsR
import com.study.chat.R as ChatR
import com.study.profile.R as ProfileR
import com.study.users.R as UsersR

class MainActivity : ElmActivity<MainEvent, MainEffect, MainState>() {
    private lateinit var binding: ActivityMainBinding

    @Inject
    lateinit var mainStore: Store<MainEvent, MainEffect, MainState>

    @Inject
    lateinit var mainActor: MainActor

    override val initEvent: MainEvent = MainEvent.Ui.Init
    override val storeHolder: StoreHolder<MainEvent, MainEffect, MainState> by fastLazy {
        createStoreHolder(mainStore)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        (applicationContext as ChatApp).appComponent.inject(this)
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.activity_main_fragment_container) as NavHostFragment
        val navController = navHostFragment.navController
        setupBottomNavigation(navController)
        setupTopNavigation(navController)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.activity_main_top_menu, menu)
        val searchItem = menu?.findItem(R.id.action_search)
        val searchView = searchItem?.actionView as? SearchView
        searchView?.maxWidth = Int.MAX_VALUE
        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let { store.accept(MainEvent.Ui.Search(query)) }
                return false
            }

            override fun onQueryTextChange(query: String?): Boolean {
                query?.let { store.accept(MainEvent.Ui.Search(query)) }
                return false
            }
        })
        return super.onCreateOptionsMenu(menu)
    }

    override fun onStop() {
        super.onStop()
        mainActor.clear()
    }

    private fun setupTopNavigation(navController: NavController) {
        val appBarConfiguration = AppBarConfiguration(
            topLevelDestinationIds = setOf(
                ProfileR.id.profileFragment,
                ChannelsR.id.holderChannelsFragment,
                UsersR.id.usersFragment,
                ChatR.id.chatFragment
            ), fallbackOnNavigateUpListener = ::onSupportNavigateUp
        )
        setSupportActionBar(binding.activityMainToolbar)
        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    private fun setupBottomNavigation(navController: NavController) {
        binding.activityMainBottomNavigation.setupWithNavController(navController)
        navController.addOnDestinationChangedListener { _, destination, arguments ->
            val isBottomNavVisible = when (destination.id) {
                ProfileR.id.profileFragment ->
                    arguments?.getInt(NavConstants.USER_ID_KEY) == NavConstants.CURRENT_USER_ID_KEY
                ChannelsR.id.holderChannelsFragment -> true
                UsersR.id.usersFragment -> true
                else -> false
            }
            binding.activityMainBottomNavigation.isVisible = isBottomNavVisible
        }
        setupTopNavigation(navController)
    }

    override fun onSupportNavigateUp() =
        findNavController(R.id.activity_main_fragment_container).navigateUp()

    override fun render(state: MainState) = Unit
}
