package com.example.mylocation.ui

import android.os.Bundle
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.mylocation.AndroidApplication.Companion.checkedItem
import com.example.mylocation.R
import com.example.mylocation.databinding.ActivityMainBinding
import com.example.mylocation.domian.model.ConstantGeneral.Companion.DARK
import com.example.mylocation.domian.model.ConstantGeneral.Companion.DEFAULT
import com.example.mylocation.domian.model.ConstantGeneral.Companion.LIGTH
import com.example.mylocation.ui.component.OptionsMenu
import com.example.mylocation.ui.component.Screen
import com.example.mylocation.ui.home.views.HomeFragment
import com.example.mylocation.ui.location.views.LocationFragment
import com.example.mylocation.ui.photo.PhotoFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initListener()
        setupViews()
        changeScreen(Screen.HomeFragment)
    }


     fun chooseThemeDialog() {

        val builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.app_name))
        val styles = arrayOf(LIGTH, DARK, DEFAULT)

        builder.setSingleChoiceItems(styles, checkedItem) { dialog, which ->

            when (which) {
                0 -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    delegate.applyDayNight()
                    recreate()
                    checkedItem=0
                    dialog.dismiss()
                }
                1 -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    delegate.applyDayNight()
                    recreate()
                    checkedItem=1
                    dialog.dismiss()
                }
                2 -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                    delegate.applyDayNight()
                    checkedItem=2
                    dialog.dismiss()
                }

            }
        }

        val dialog = builder.create()
        dialog.show()
    }

    private fun setupViews() {
        setUpDrawerLayout()
    }

    private fun changeFragment(fragment: Fragment) {
        val ft: FragmentTransaction = supportFragmentManager.beginTransaction()
        ft.remove(fragment)
        ft.replace(R.id.Fragment_principal, fragment)
        ft.commit()
    }

    private fun setUpDrawerLayout() {
        setSupportActionBar(binding.toolbar)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return super.onOptionsItemSelected(item)
    }

    private fun initListener(){
        binding.apply {

            tvMenuId.setOnClickListener {
                drawerLayout.openDrawer(GravityCompat.START)
            }

            navigationView.setNavigationItemSelectedListener {
                when (it.itemId) {
                    R.id.location -> {
                        changeItemMenu(OptionsMenu.LocationFragment)
                    }
                    R.id.photo -> {
                        changeItemMenu(OptionsMenu.PhotoFragment)
                    }
                }
                drawerLayout.closeDrawer(Gravity.LEFT)
                true
            }

            tvSalir.setOnClickListener {
                finish()
            }
        }
    }

    private fun changeItemMenu(typeFragment: OptionsMenu) {
        when(typeFragment) {
            OptionsMenu.LocationFragment -> {
                openLocationFragment()
            }
            OptionsMenu.PhotoFragment ->{
                openPhotoFragment()
            }
        }
    }

    fun changeScreen(typeScreen: Screen)
    {
        when(typeScreen)
        {
            Screen.MainActivity -> {
                binding.toolbar.visibility = View.VISIBLE
            }
            Screen.HomeFragment ->{
                openHomeFragment()
            }
        }
    }

    private fun openHomeFragment() {
        changeFragment(HomeFragment.newInstance())
    }

    private fun openLocationFragment() {
       changeFragment(LocationFragment.newInstance())
    }

    private fun openPhotoFragment() {
        changeFragment(PhotoFragment.newInstance())
    }
}