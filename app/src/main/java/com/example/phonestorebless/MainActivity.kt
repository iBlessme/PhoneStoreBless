package com.example.phonestorebless

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.example.phonestorebless.client.profile.FragmentProfile
import com.example.phonestorebless.client.setings.FragmentSetings
import com.example.phonestorebless.client.shop.FragmentShop
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val shopFragment = FragmentShop()
        val profileFragment = FragmentProfile()
        val settingsFragment = FragmentSetings()

        makeCurrentFragment(shopFragment)
        bottomNavigationView.setOnNavigationItemSelectedListener {
            when (it.itemId){
                R.id.ic_shopFragment -> makeCurrentFragment(shopFragment)
                R.id.ic_profileFragment -> makeCurrentFragment(profileFragment)
                R.id.ic_settingsFragment -> makeCurrentFragment(settingsFragment)
            }
            true
        }

//        val navController = Navigation.findNavController(this, R.id.nav_host_fragment)
//        NavigationUI.setupWithNavController(bottomNavigationView, navController)
        }

    private fun makeCurrentFragment(fragment:Fragment) =
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.nav_host_fragment, fragment)
                commit()
            }
}