package com.example.phonestorebless.personal.admin

import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.phonestorebless.R
import com.example.phonestorebless.client.profile.FragmentProfile
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.fragment_add_pos_admin.*
import kotlinx.android.synthetic.main.fragment_sign_up.*

class FragmentAddPosAdmin : Fragment() {

    //Обьявляем переменные
    private lateinit var mAuth: FirebaseAuth
    var databaseReference: DatabaseReference? = null
    var database: FirebaseDatabase? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_add_pos_admin, container, false)
    }
    //Показ диалогового окна
    fun Fragment.showToast(message: String) {
        Toast.makeText(this.context, message, Toast.LENGTH_SHORT).show()
    }

    override fun onStart() {
        super.onStart()

        mAuth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        databaseReference = database?.reference!!.child("Personals")

        btnAddPersonal.setOnClickListener {
            addPerson()
        }
    }
    private fun addPerson(){
        when {
            TextUtils.isEmpty(edtNamePersonal.text.toString()) -> {
                showToast("Укажите имя сотрудника!")
                return
            }
            TextUtils.isEmpty(edtMailPersonal.text.toString()) -> {
                showToast("Укажите почту сотрудника!")
                return
            }
            TextUtils.isEmpty(edtPasswordPersonal.text.toString()) -> {
                showToast("Укажите пароль сотрудника!")
                return
            }
            (!rtbtnAdmin.isChecked || !rtbtnStorage.isChecked) -> {
                showToast("Укажите должность сотрудника!")
                return
            }
            else -> mAuth.createUserWithEmailAndPassword(edtMailPersonal.text.toString(), edtPasswordPersonal.text.toString())
            .addOnCompleteListener {
                if(it.isSuccessful){
                    val currentUser = mAuth.currentUser
                    val currentUSerDb = databaseReference?.child((currentUser?.uid!!))

                    var position: String = ""
                    when{
                        rtbtnAdmin.isChecked -> position = "admin"
                        rtbtnStorage.isChecked -> position = "storage"
                    }

                    currentUSerDb?.child("mail")
                        ?.setValue(edtMailPersonal.text.toString())
                    currentUSerDb?.child("password")
                        ?.setValue(edtPasswordPersonal.text.toString())
                    currentUSerDb?.child("name")
                        ?.setValue(edtNamePersonal.text.toString())
                    currentUSerDb?.child("position")
                        ?.setValue(position)
                    showToast("Вы успешно зарегистрировались!")
//                    replaceFragment(FragmentProfile())
                }else{
                    showToast("Не удалось создать нового пользователя")
                }
                }
            }
        }
    }