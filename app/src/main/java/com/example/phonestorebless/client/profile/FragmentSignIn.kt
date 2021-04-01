package com.example.phonestorebless.client.profile

import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.phonestorebless.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.fragment_sign_in.*

class FragmentSignIn : Fragment() {

    private lateinit var mAuth: FirebaseAuth
    var databaseReference: DatabaseReference? = null
    var database: FirebaseDatabase? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_sign_in, container, false)
    }

    override fun onStart() {
        super.onStart()

        mAuth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        databaseReference = database?.reference!!.child("Users")

        textViewGoAuth.setOnClickListener {
            goSignUp()
        }

        btnSignIn.setOnClickListener {
            SignIn()
        }
    }
    private fun goSignUp(){
        replaceFragment(FragmentSignUp())
    }

    private fun SignIn(){
        when{
            TextUtils.isEmpty(edtMail.text.toString()) ->{
                showToast("Введите почту!")
            }
            TextUtils.isEmpty(edtPassword.text.toString()) ->{
                showToast("Введите пароль!")
            }
            else -> mAuth.signInWithEmailAndPassword(edtMail.text.toString(), edtPassword.text.toString())
                .addOnCompleteListener {
                    if(it.isSuccessful){
                        showToast("Вы успешно авторизовались!")
                        replaceFragment(FragmentProfile())
                    }else{
                        showToast("Вы ввели некоректные данные!")
                    }
                }


        }
    }
    //Показ диалогового окна
    fun Fragment.showToast(message: String) {
        Toast.makeText(this.context, message, Toast.LENGTH_SHORT).show()
    }
    //переход на другой фрагмент
    fun Fragment.replaceFragment(fragment: Fragment) {
        fragmentManager?.beginTransaction()
            ?.addToBackStack(null)
            ?.replace(R.id.nav_host_fragment, fragment)
            ?.commit()
    }
}