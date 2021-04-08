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
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_sign_up.*

class FragmentSignUp : Fragment() {

    //Обьявляем переменные
    private lateinit var mAuth: FirebaseAuth
    var databaseReference: DatabaseReference? = null
    var database: FirebaseDatabase? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_sign_up, container, false)
    }


    override fun onStart() {
        super.onStart()
        mAuth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        databaseReference = database?.reference!!.child("Users")
        //обработчик кнопки регистрации
        btnSignUp.setOnClickListener {
            sigUp()

        }
        txtViewGoAuth.setOnClickListener {
            replaceFragment(FragmentSignIn())
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
    // Функция регистрации пользователя
    private fun sigUp(){
            when {
                TextUtils.isEmpty(edtMail.text.toString()) -> {
                    showToast("Заполните почту")
                    return
                }
                TextUtils.isEmpty(edtName.text.toString()) -> {
                    showToast("Заполните Имя")
                    return
                }
                TextUtils.isEmpty(edtNumberPhone.text.toString()) -> {
                    showToast("Заполните Номер телефона")
                    return
                }
                TextUtils.isEmpty(edtPassword.text.toString()) -> {
                    showToast("Заполните Пароль")
                    return
                }
                edtPassword.text.length < 6 ->{
                    showToast("Пароль дожен быть не менее 6 символов")
                    return
                }
                edtNumberPhone.text.length != 11 ->{
                    showToast("Номер телефона должен содержать 11 цифр!")
                }
               else -> mAuth.createUserWithEmailAndPassword(edtMail.text.toString(), edtPassword.text.toString())
                       .addOnCompleteListener {
                           if(it.isSuccessful){
                               val currentUser = mAuth.currentUser
                               val currentUSerDb = databaseReference?.child((currentUser?.uid!!))

                               currentUSerDb?.child("mail")
                                       ?.setValue(edtMail.text.toString())
                               currentUSerDb?.child("password")
                                       ?.setValue(edtPassword.text.toString())
                               currentUSerDb?.child("name")
                                       ?.setValue(edtName.text.toString())
                               currentUSerDb?.child("numberPhone")
                                       ?.setValue(edtNumberPhone.text.toString())
                               currentUSerDb?.child("photoUrl")
                                       ?.setValue(currentUser?.uid)
//                               currentUSerDb?.child("position")
//                                       ?.setValue("client")
                               showToast("Вы успешно зарегистрировались!")
                               replaceFragment(FragmentProfile())
                           }else{
                               showToast("Данный пользователь уже существует")
                           }
                       }



            }


        }





}