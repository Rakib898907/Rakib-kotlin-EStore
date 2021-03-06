package com.rakibstudio.e_store.ui.activity

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.rakibstudio.e_store.R
import com.rakibstudio.e_store.databinding.ActivityMainBinding
import com.rakibstudio.e_store.repository.ProductRepository
import com.rakibstudio.e_store.viewmodel.ProductViewModel
import com.rakibstudio.e_store.viewmodel.ViewModelProductProvider
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity(), NavController.OnDestinationChangedListener {

    //Binding
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding

    //BottomBar
    private lateinit var navHostFragment: NavHostFragment
    private lateinit var navBottomBar: BottomNavigationView
    private lateinit var navController: NavController

    //ViewModel
    lateinit var viewModel: ProductViewModel
    lateinit var repository: ProductRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        //init NavBar & Setting it up
        navBottomBar = binding!!.navBottom
        navHostFragment =
            supportFragmentManager.findFragmentById(R.id.main_nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        navBottomBar.setupWithNavController(navHostFragment.findNavController())
        navController.addOnDestinationChangedListener(this)
        navBottomBar.itemIconTintList = null

        //init ViewModel
        repository = ProductRepository()
        viewModel = ViewModelProvider(
            this,
            ViewModelProductProvider(repository)
        ).get(ProductViewModel::class.java)

    }

    override fun onDestinationChanged(
        controller: NavController,
        destination: NavDestination,
        arguments: Bundle?
    ) {

        when (destination.id) {
            R.id.exploreFragment -> {
                binding!!.editTextSearch.visibility = View.VISIBLE
                binding!!.cameraImageButton.visibility = View.VISIBLE
                binding!!.navBottom.visibility = View.VISIBLE
            }

            R.id.cartFragment, R.id.accountFragment, R.id.cardsFragment, R.id.addressFragment -> {
                binding!!.editTextSearch.visibility = View.GONE
                binding!!.cameraImageButton.visibility = View.GONE
                binding!!.navBottom.visibility = View.VISIBLE
            }

            else -> {
                binding!!.editTextSearch.visibility = View.GONE
                binding!!.cameraImageButton.visibility = View.GONE
                binding!!.navBottom.visibility = View.GONE
            }
        }

    }
}