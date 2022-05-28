package com.isilsubasi.chatapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.isilsubasi.chatapp.databinding.ActivityDetailBinding
import com.isilsubasi.chatapp.databinding.ActivityMainBinding

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    //firebase auth
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
    }

    private fun init(){
        //init firebase auth
        firebaseAuth= FirebaseAuth.getInstance()
        checkUser()
        onClickListener()

    }

    private fun checkUser(){

        //get current user
        val firebaseUser=firebaseAuth.currentUser
        if (firebaseUser==null){
            startActivity(Intent(this@DetailActivity,MainActivity::class.java))
            finish()
        }else{

            val email=firebaseUser.email
            binding.txtEmail.setText(email)
        }

    }

    private fun onClickListener(){
        binding.btnLogout.setOnClickListener {
            firebaseAuth.signOut()
            checkUser()
        }

    }


}