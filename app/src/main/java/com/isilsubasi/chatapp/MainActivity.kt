package com.isilsubasi.chatapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthCredential
import com.google.firebase.auth.GoogleAuthProvider
import com.isilsubasi.chatapp.databinding.ActivityMainBinding
import java.lang.Exception

class MainActivity : AppCompatActivity() {

    //viewBinding
    private lateinit var binding: ActivityMainBinding

    private lateinit var googleSignInClient : GoogleSignInClient
    private lateinit var firebaseAuth: FirebaseAuth

    //constants
    private companion object{
        private const val RC_SIGN_IN=100
        private const val TAG="GOOGLE_SIGN_IN_TAG"

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()


    }

    private fun init(){

        configureGoogleSignIn()
    }



    private fun configureGoogleSignIn(){

        val googleSignInOptions=GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this,googleSignInOptions)
        firebaseAuth= FirebaseAuth.getInstance()
        checkUser()
        onClickListener()



    }


    private fun onClickListener(){
        binding.btnGoogleSignIn.setOnClickListener {
            Log.d(TAG,"begin google sign in ")
            val intent=googleSignInClient.signInIntent
            startActivityForResult(intent, RC_SIGN_IN)


        }
    }


    private fun checkUser(){
         //check if user is logged in or not
        val firebaseUser=firebaseAuth.currentUser
        if (firebaseUser!=null){
            startActivity(Intent(this@MainActivity,DetailActivity::class.java))
            finish()
        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode== RC_SIGN_IN){
            Log.d(TAG,"onActivityResult : Google Sign In intent result")
            val accountTask=GoogleSignIn.getSignedInAccountFromIntent(data)

            try {

                val account = accountTask.getResult(ApiException::class.java)
                firebaseAuthWithGoogleAccount(account)


            }catch (e : Exception){

                Log.d(TAG,"onActivityResult : ${e.message}")
            }

        }

    }

    private fun firebaseAuthWithGoogleAccount(account: GoogleSignInAccount?) {

        Log.d(TAG,"firebaseAuthWithGoogleAccount : begin firebase auth with google account")

        val credential = GoogleAuthProvider.getCredential(account!!.idToken,null)
        firebaseAuth.signInWithCredential(credential)
            .addOnSuccessListener {authResult ->
                //login success
                Log.d(TAG,"firebaseAuthWithGoogleAccount : LoggedIn")

                //get loggedIn user
                val firebaseUser=firebaseAuth.currentUser
                //get user info
                val uid=firebaseUser!!.uid
                val email=firebaseUser!!.email

                Log.d(TAG,"firebaseAuthWithGoogleAccount : uid = ${uid} ")
                Log.d(TAG,"firebaseAuthWithGoogleAccount : email = ${email}")

                //check if user is new or existing
                if(authResult.additionalUserInfo!!.isNewUser){

                    //user is new
                    Log.d(TAG,"firebaseAuthWithGoogleAccount : Account created..\n $email")
                    Toast.makeText(this@MainActivity,"Account Created.. \n$email",Toast.LENGTH_LONG).show()

                }
                else{
                    Log.d(TAG,"firebaseAuthWithGoogleAccount : Existing user..\n $email")
                    Toast.makeText(this@MainActivity,"LoggedIn.. \n$email",Toast.LENGTH_LONG).show()
                }

                //start profile activity
                startActivity(Intent(this@MainActivity,DetailActivity::class.java))
                finish()

            }

            .addOnFailureListener { e ->
                Log.d(TAG,"firebaseAuthWithGoogleAccount : Loggin failed due to ${e.message}")
                Toast.makeText(this@MainActivity,"Loggin failed due to ${e.message} ",Toast.LENGTH_LONG).show()
            }

    }


}