package com.chloe.shopshare

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import androidx.activity.viewModels
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.chloe.shopshare.databinding.ActivityMainBinding
import com.chloe.shopshare.ext.getVmFactory
import com.chloe.shopshare.util.CurrentFragmentType
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.android.material.bottomnavigation.BottomNavigationMenuView
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.launch


class MainActivity : BaseActivity() {

    val viewModel by viewModels<MainViewModel> { getVmFactory() }

    private lateinit var binding: ActivityMainBinding

    // get the height of status bar from system
    private val statusBarHeight: Int
        get() {
            val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
            return when {
                resourceId > 0 -> resources.getDimensionPixelSize(resourceId)
                else -> 0
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        setupToolbar()
        setupBottomNav()
        setupNavController()
        setUpBadge()
    }

    private fun setupNavController() {
        findNavController(R.id.myNavHostFragment).addOnDestinationChangedListener { navController: NavController, _: NavDestination, _: Bundle? ->
            viewModel.currentFragmentType.value = when (navController.currentDestination?.id) {
                R.id.homeFragment -> CurrentFragmentType.HOME
                R.id.hostFragment -> CurrentFragmentType.HOST
                R.id.requestFragment -> CurrentFragmentType.REQUEST
                R.id.detailFragment -> CurrentFragmentType.DETAIL
                R.id.requestDetailFragment -> CurrentFragmentType.REQUEST_DETAIL
                R.id.orderFragment -> CurrentFragmentType.ORDER
                R.id.likeFragment -> CurrentFragmentType.LIKE
                R.id.profileFragment -> CurrentFragmentType.PROFILE
                R.id.myHostFragment -> CurrentFragmentType.SHOP
                R.id.manageFragment -> CurrentFragmentType.MANAGE
                R.id.myOrderFragment -> CurrentFragmentType.MY_ORDER
                R.id.myOrderListFragment -> CurrentFragmentType.MY_ORDER
                R.id.trackFragment -> CurrentFragmentType.TRACK
                R.id.myRequestFragment -> CurrentFragmentType.MY_REQUEST
                R.id.myRequestListFragment -> CurrentFragmentType.MY_REQUEST
                R.id.loginFragment -> CurrentFragmentType.LOGIN
                R.id.notifyFragment -> CurrentFragmentType.NOTIFY
                R.id.searchFragment -> CurrentFragmentType.SEARCH
                R.id.chatFragment -> CurrentFragmentType.CHAT
                R.id.chatRoomFragment -> CurrentFragmentType.CHAT_ROOM
                R.id.resultFragment -> CurrentFragmentType.RESULT
                else -> viewModel.currentFragmentType.value
            }
        }
    }


    /**
     * Set up [BottomNavigationView], add badge view through [BottomNavigationMenuView] and [BottomNavigationItemView]
     * to display the count of Cart
     */
    private fun setupBottomNav() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.myNavHostFragment) as NavHostFragment
        val navController = navHostFragment.navController

        findViewById<BottomNavigationView>(R.id.bottomNavView).setupWithNavController(navController)

        viewModel.navigateToHomeByBottomNav.observe(this, Observer {
            it?.let {
                binding.bottomNavView.selectedItemId = R.id.homeFragment
                viewModel.onHomeNavigated()
            }
        })

    }

    private fun setUpBadge() {
        val navMenu = binding.bottomNavView
        val notifyBadge = navMenu.getOrCreateBadge(R.id.profileFragment)
        notifyBadge.maxCharacterCount = 99
        notifyBadge.horizontalOffset = 20
        notifyBadge.verticalOffset = 20
        notifyBadge.backgroundColor = ContextCompat.getColor(this, R.color.textColorError)

        viewModel.notify.observeForever {
            when (it.isNullOrEmpty()) {
                true -> notifyBadge.isVisible = false
                else -> {
                    notifyBadge.isVisible = true
                    notifyBadge.number = it.size
                }
            }
        }
    }


    /**
     * Set up the layout of [Toolbar], according to whether it has cutout
     */
    private fun setupToolbar() {

        launch {
            val oriStatusBarHeight =
                resources.getDimensionPixelSize(R.dimen.height_status_bar_origin)
            binding.toolbar.setPadding(0, 0, 0, 0)
            val layoutParams = Toolbar.LayoutParams(
                Toolbar.LayoutParams.WRAP_CONTENT,
                Toolbar.LayoutParams.WRAP_CONTENT
            )
            layoutParams.gravity = Gravity.CENTER
            when (Build.MODEL) {
                "Pixel 5" -> {
                    Log.i("Chloe", "Build.MODEL is ${Build.MODEL}")
                }
                else -> {
                    layoutParams.topMargin = statusBarHeight - oriStatusBarHeight
                }
            }
        }
        Log.i("Chloe", "====== ${Build.MODEL} ======")
    }
}
