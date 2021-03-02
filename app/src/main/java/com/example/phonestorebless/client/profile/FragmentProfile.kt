package com.example.phonestorebless.client.profile

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.media.Image
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.PagerAdapter
import com.example.phonestorebless.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.core.FirestoreClient
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.fragment_sign_in.*
import java.io.ByteArrayOutputStream

class FragmentProfile : Fragment() {

    lateinit var storage:  FirebaseStorage
    private lateinit var mAuth: FirebaseAuth
    lateinit var auth: FirebaseAuth
    var databaseReference: DatabaseReference? = null
    var database: FirebaseDatabase? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onStart() {
        super.onStart()

        email.isVisible = false
        nameClient.isVisible = false
        PhoneClient.isVisible = false

        //Инициализируем
        storage = FirebaseStorage.getInstance()
        mAuth = FirebaseAuth.getInstance()
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        databaseReference = database?.reference!!.child("Users")
        //Проверка на предыдущий вход
        checkUser()
        //Обработчик выхода из профиля
        exitProfile.setOnClickListener {
            replaceFragment(FragmentSignUp())
            mAuth.signOut()
        }

        //Обработчик добавления фотографии
        addPictureProfile.setOnClickListener {
            openGallerey()
        }
    }


    //Функция проверки пользователя
    private fun checkUser() {
        mAuth = FirebaseAuth.getInstance()
        val currentUser = mAuth.currentUser
        if (currentUser != null) {
            //Заполнение пользователя
            loadProfile()
        } else {
            replaceFragment(FragmentSignIn())
        }
    }

    //Функция заргузки пользователя
    private fun loadProfile() {
        val user = auth.currentUser
        val unreferenced = databaseReference?.child(user?.uid!!)

        email.text = user?.email

        unreferenced?.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {


                nameClient.text = snapshot.child("name").value.toString()
                nameClient.isVisible = true
                PhoneClient.text = snapshot.child("numberPhone").value.toString()
                PhoneClient.isVisible = true
                email.isVisible = true

            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }
    //Функция загрузки изображения пользователя
    private fun loadPictureProfile(){

    }
    private fun addPictureInStorage(){
        val user = auth.currentUser
        val uid = user?.uid.toString()

        val storageRef = storage.reference
        val pictureRef = storageRef.child(uid)
        val profileImageRef = storageRef.child("users/${uid}")

        imageProfile.isDrawingCacheEnabled = true
        imageProfile.buildDrawingCache()
        val bitmap = (imageProfile.drawable as BitmapDrawable).bitmap
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()

        val uploadTask = profileImageRef.putBytes(data)
        uploadTask.addOnFailureListener {

        }.addOnSuccessListener { taskSnapshot ->

        }
    }

    //Смена фрагмента
    fun Fragment.replaceFragment(fragment: Fragment) {
        fragmentManager?.beginTransaction()
            ?.addToBackStack(null)
            ?.replace(R.id.nav_host_fragment, fragment)
            ?.commit()
    }


    fun Fragment.openGallerey() {

        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, 1)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && requestCode == 1){
            imageProfile.setImageURI(data?.data)

          addPictureInStorage()
        }
    }
}




