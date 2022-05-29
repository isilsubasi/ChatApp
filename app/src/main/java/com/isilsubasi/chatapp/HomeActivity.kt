package com.isilsubasi.chatapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.isilsubasi.chatapp.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    //firebase auth
    private lateinit var firebaseAuth: FirebaseAuth

    //user List
    private lateinit var userList: ArrayList<User>
    private lateinit var userAdapter: UserAdapter
    private lateinit var mDbRef : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
    }

    private fun init() {

        userList= ArrayList()
        userAdapter= UserAdapter(this,userList)

        binding.recylerView.apply {
            layoutManager=LinearLayoutManager(applicationContext)
            adapter=userAdapter
        }

        //checkUser()
        //init firebase auth dbref
        firebaseAuth = FirebaseAuth.getInstance()
        mDbRef=FirebaseDatabase.getInstance().getReference()
        mDbRef.child("user").addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {

                userList.clear()
                for (postSnapshot in snapshot.children ){
                    val currentUser = postSnapshot.getValue(User::class.java)
                    userList.add(currentUser!!)


                }
                userAdapter.notifyDataSetChanged()

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })






    }

    private fun checkUser() {

        //get current user
        val firebaseUser = firebaseAuth.currentUser
        if (firebaseUser == null) {
            startActivity(Intent(this@HomeActivity, MainActivity::class.java))
            finish()
        } else {

            val email = firebaseUser.email

        }

    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.home_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menuLogout -> {
                firebaseAuth.signOut()
                checkUser()
            }
        }
        return super.onOptionsItemSelected(item)


    }

}