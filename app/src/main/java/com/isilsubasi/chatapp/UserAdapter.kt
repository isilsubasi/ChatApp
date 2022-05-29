package com.isilsubasi.chatapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.isilsubasi.chatapp.databinding.UserLayoutBinding

class UserAdapter (val context : Context , val userList : ArrayList<User>): RecyclerView.Adapter<UserAdapter.UserViewHolder> () {


    inner class UserViewHolder(val binding: UserLayoutBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserAdapter.UserViewHolder {
        val binding = UserLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserAdapter.UserViewHolder, position: Int) {

        val currentUser = userList[position]
        holder.binding.txtEmail.text=currentUser.email


    }

    override fun getItemCount(): Int {
        return userList.size
    }




}