package com.example.phonestorebless.personal.admin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.phonestorebless.R
import kotlinx.android.synthetic.main.activity_admin.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.bottomNavigationView

class AdminActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)

        val changedFragment = FragmentChangedPosAdmin()
        val addPosFragment = FragmentAddPosAdmin()
        val settingAdmin = FragmentSettingsAdmin()
        val test3 = "test"

        makeCurrentFragment(changedFragment)
        bottomNavigationViewAdmin.setOnNavigationItemSelectedListener {
            when (it.itemId){
                R.id.ic_ChangePositionAdmin -> makeCurrentFragment(changedFragment)
                R.id.ic_AddPositionAdmin -> makeCurrentFragment(addPosFragment)
                R.id.ic_settingsAdmin -> makeCurrentFragment(settingAdmin)
            }
            true
        }

    }
    private fun makeCurrentFragment(fragment: Fragment) =
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.nav_host_fragment_admin, fragment)
                commit()
            }
}