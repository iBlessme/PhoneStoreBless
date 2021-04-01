package com.example.phonestorebless.client.setings


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.phonestorebless.personal.ActivitySignInJob
import com.example.phonestorebless.R
import kotlinx.android.synthetic.main.fragment_setings.*

@Suppress("DEPRECATION")
class FragmentSetings : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_setings, container, false)
    }

    override fun onStart() {
        super.onStart()
        enterJob.setOnClickListener {
            goJob()
        }

    }
    private fun goJob(){
        replaceActivity(ActivitySignInJob())
    }
    fun Fragment.replaceActivity(activity: Activity) {
        val intent = Intent(this.context, activity::class.java)
        startActivity(intent)
    }


}