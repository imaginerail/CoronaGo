package com.aneeq.coronago.activity

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.FrameLayout
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.aneeq.coronago.R
import com.aneeq.coronago.fragment.CountriesFragment
import com.aneeq.coronago.fragment.HomeFragment
import com.aneeq.coronago.fragment.IndiaFragment
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {
    lateinit var drawerLayout: DrawerLayout
    lateinit var coordinatorLayout: CoordinatorLayout
    lateinit var toolbar: Toolbar
    lateinit var frame: FrameLayout
    lateinit var navigationView: NavigationView
    lateinit var collapsingtoolbar: CollapsingToolbarLayout
    var previousMenuItem: MenuItem? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        drawerLayout = findViewById(R.id.drawerLayout)
        coordinatorLayout = findViewById(R.id.coordinatorLayout)
        toolbar = findViewById(R.id.toolbar)
        frame = findViewById(R.id.frame)
        navigationView = findViewById(R.id.navigationView)
        collapsingtoolbar = findViewById(R.id.collapsingtoolbar)
        setUpToolbar()
        openHome()

        val actionBarDrawerToggle = ActionBarDrawerToggle(
            this@MainActivity, drawerLayout,
            R.string.open_drawer,
            R.string.close_drawer
        )
        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()

        navigationView.setNavigationItemSelectedListener {

            if (previousMenuItem != null) {
                previousMenuItem?.isChecked = false
            }
            it.isCheckable = true
            it.isChecked = true
            previousMenuItem = it        //highlight menu


            when (it.itemId) {
                R.id.itGlobal -> {
                    openHome()
                    drawerLayout.closeDrawers()

                }
                R.id.itCountry -> {
                    supportFragmentManager.beginTransaction()
                        .replace(
                            R.id.frame,
                            CountriesFragment()
                        )

                        .commit()
                    drawerLayout.closeDrawers()
                    supportActionBar?.title = "COUNTRY STATISTICS"
                }
                R.id.itIndia -> {
                    supportFragmentManager.beginTransaction()
                        .replace(
                            R.id.frame,
                            IndiaFragment()
                        )

                        .commit()
                    drawerLayout.closeDrawers()
                    supportActionBar?.title = "INDIA STATISTICS"
                }
            }
            return@setNavigationItemSelectedListener true
        }

    }

    fun setUpToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.title = "GLOBAL STATISTICS"
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        val id = item.itemId
        if (id == android.R.id.home) {
            drawerLayout.openDrawer(GravityCompat.START)
        }
        return super.onOptionsItemSelected(item)
    }

    fun openHome() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.frame, HomeFragment())
            .commit()
        supportActionBar?.title = "GLOBAL STATISTICS"
        navigationView.setCheckedItem(R.id.itGlobal)
    }

    override fun onBackPressed() {
        when (supportFragmentManager.findFragmentById(R.id.frame)) {
            !is HomeFragment -> openHome()
            else -> {
                super.onBackPressed()
            }
        }
        drawerLayout.closeDrawers()
    }
}



