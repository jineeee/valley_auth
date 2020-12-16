package com.example.auth_client.ui

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.auth_client.R
import com.example.auth_client.data.model.UserInfo
import kotlinx.android.synthetic.main.item_user_info.*
import kotlinx.android.synthetic.main.item_user_info.view.*

class UserListAdapter(private val context: Context) : RecyclerView.Adapter<UserInfoViewHolder>(){

    var data = listOf<UserInfo>()

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):  UserInfoViewHolder{
        val view = LayoutInflater.from(context).inflate(R.layout.item_user_info, parent, false)
        return UserInfoViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: UserInfoViewHolder, position: Int) {
        holder.bind(data[position])
        holder.itemView.setOnClickListener {
            val intent = Intent(context, UserEditActivity::class.java)
            intent.putExtra("id", data[position].id)
            intent.putExtra("name", data[position].name)
            intent.putExtra("department", data[position].department)
            intent.putExtra("rank", data[position].rank)
            context.startActivity(intent)
        }
    }

}

class UserInfoViewHolder(view : View) : RecyclerView.ViewHolder(view){

    val name : TextView = view.findViewById(R.id.item_user_info_tv_name)
    val department : TextView = view.findViewById(R.id.item_user_info_tv_department)
    val rank : TextView = view.findViewById(R.id.item_user_info_tv_rank)

    fun bind(data: UserInfo){
        name.text = data.name
        department.text = data.department
        rank.text = data.rank
    }
}