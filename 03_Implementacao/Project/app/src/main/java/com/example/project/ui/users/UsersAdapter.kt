package com.example.project.ui.users

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.project.R
import com.example.project.data.model.Game
import com.example.project.data.model.User
import com.example.project.databinding.ItemGameBinding
import com.example.project.databinding.ItemUserBinding
import com.example.project.ui.events.OnItemClickedListener

class UsersAdapter (
    private val allUsersList: List<User>,
    private val itemClickedListener: ((User) -> Unit)? = null,
    private val context: Context
) : RecyclerView.Adapter<UsersAdapter.ViewHolder>()
{
    class ViewHolder(itemView: View, private val context: Context) : RecyclerView.ViewHolder(itemView) {
        private val viewBinding = ItemUserBinding.bind(itemView)

        fun bindView(userItem: User, itemClickedListener: ((User) -> Unit)?) {

            viewBinding.user = userItem

            Glide.with(context)
                .load(userItem.imageUrl)
                .into(viewBinding.userImage)

            itemView.setOnClickListener {
                //Toast.makeText(itemView.context, String.format("Click in %s Region", regionItem.name), Toast.LENGTH_LONG).show()
                itemClickedListener?.invoke(userItem)
            };
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.item_user, parent, false)
        return ViewHolder(view, context)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val itemAllUsers = allUsersList[position]
        holder.bindView(itemAllUsers, itemClickedListener)
    }

    override fun getItemCount(): Int {
        return allUsersList.size
    }

}