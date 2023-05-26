package com.example.submissiongithubuser.ui.main

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.submissiongithubuser.User
import com.example.submissiongithubuser.databinding.ItemDetailBinding

class ListUserAdapter : RecyclerView.Adapter<ListUserAdapter.UserViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): UserViewHolder {
        val view=
            ItemDetailBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return UserViewHolder(view)
    }

    private val listUser = ArrayList<User>()
    fun setListData(users: List<User>) {
        listUser.clear()
        listUser.addAll(users)
        notifyDataSetChanged()
        Log.d("TAG", "$listUser")
    }

    inner class UserViewHolder(private val binding: ItemDetailBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(user: User) {
            binding.root.setOnClickListener {
                onItemClickCallback?.onItemClicked(user)
            }
            binding.apply {
                Glide.with(itemView)
                    .load(user.avatar_url)
                    .centerCrop()
                    .apply(RequestOptions().override(50, 50))
                    .into(binding.ivPhoto)
                tvName.text = user.login
            }
        }
    }

    override fun onBindViewHolder(viewHolder: UserViewHolder, position: Int) {
        viewHolder.bind(listUser[position])
    }

    override fun getItemCount(): Int = listUser.size

    private var onItemClickCallback: OnItemClickCallback? = null

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }
    interface OnItemClickCallback {
        fun onItemClicked(data: User)
    }
}