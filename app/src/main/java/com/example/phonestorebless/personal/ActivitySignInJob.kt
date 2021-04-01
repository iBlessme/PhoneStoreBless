package com.example.phonestorebless

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.example.phonestorebless.client.profile.FragmentProfile
import com.example.phonestorebless.personal.admin.AdminActivity
import com.example.phonestorebless.personal.bookKeep.BookKeepingActivity
import com.example.phonestorebless.personal.humanDep.HumanDepActivity
import com.example.phonestorebless.personal.storage.StorageActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_sign_in_job.*
import kotlinx.android.synthetic.main.activity_sign_in_job.btnSignIn
import kotlinx.android.synthetic.main.activity_sign_in_job.edtMail
import kotlinx.android.synthetic.main.activity_sign_in_job.edtPassword
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.fragment_sign_in.*

class ActivitySignInJob : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth
    lateinit var auth: FirebaseAuth
    var databaseReference: DatabaseReference? = null
    var database: FirebaseDatabase? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in_job)

        mAuth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        databaseReference = database?.reference!!.child("Users")
        auth = FirebaseAuth.getInstance()

        btnGoBack.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        btnSignIn.setOnClickListener {
            letsWork()
        }



    }
    fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
    private fun letsWork() {
        when {
            TextUtils.isEmpty(edtMail.text.toString()) -> {
                showToast("Введите почту!")
            }
            TextUtils.isEmpty(edtPassword.text.toString()) -> {
                showToast("Введите пароль!")
            }
            else ->
                mAuth.signInWithEmailAndPassword(
                    edtMail.text.toString(),
                    edtPassword.text.toString()
                )
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            checkPosition()
                            showToast("Вы успешно авторизовались!")

                        } else {
                            showToast("Вы ввели некоректные данные!")
                        }

                    }
        }
    }
    private fun checkPosition(){
        val user = mAuth.currentUser
        val unreferenced = databaseReference?.child(user?.uid!!)

        unreferenced?.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                    val position = snapshot.child("position").value.toString()
                    if (position == "admin")
                        replaceActivity(AdminActivity())
                    else if (position == "bookKeep")
                        replaceActivity(BookKeepingActivity())
                    else if (position == "storage")
                    replaceActivity(StorageActivity())
                    else if (position == "humanDep")
                    replaceActivity(HumanDepActivity())

            }

            override fun onCancelled(error: DatabaseError) {

            }
        })

    }
    fun replaceActivity(activity: Activity){
        val intent = Intent(this, activity::class.java)
        startActivity(intent)
    }
}